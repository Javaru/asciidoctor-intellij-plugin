package org.asciidoc.intellij.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElementVisitor;
import icons.AsciiDocIcons;
import org.asciidoc.intellij.inspections.AsciiDocVisitor;
import org.asciidoc.intellij.lexer.AsciiDocTokenTypes;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * @author yole
 */
public class AsciiDocSection extends ASTWrapperPsiElement implements AsciiDocSelfDescribe {
  public AsciiDocSection(@NotNull ASTNode node) {
    super(node);
  }

  public String getTitle() {
    ASTNode heading = getNode().findChildByType(AsciiDocTokenTypes.HEADING);
    if (heading != null) {
      return trimHeading(heading.getText());
    }
    return "<untitled>";
  }

  // taken from Asciidoctor (rx.rb#InvalidSectionIdCharsRx)
  private static final Pattern INVALID_SECTION_ID_CHARS = Pattern.compile("<[^>]+>|&(?:[a-z][a-z]+\\d{0,2}|#\\d\\d\\d{0,4}|#x[\\da-f][\\da-f][\\da-f]{0,3});|[^ \\w\\-.]+?");

  /**
   * Produces the ID from a section like Asciidoctor (section.rb#generate_id).
   * If there are duplicate IDs in the rendered document, they receive a suffix (_num); this is not included here.
   */
  public String getAutogeneratedId() {
    // remove invalid characters and add prefix
    String key = "_" + INVALID_SECTION_ID_CHARS.matcher(getTitle().toLowerCase(Locale.US)).replaceAll("");
    // transform some characters to separator
    key = key.replaceAll("[ _.-]", "_");
    // remove duplicates separators
    key = key.replaceAll("__", "_");
    // remove separator at end
    key = StringUtil.trimEnd(key, "_");
    return key;
  }

  /**
   * Compare a ID to the automatically generated ID of this section. Will ignore any numeric suffix in the ID.
   */
  public boolean matchesAutogeneratedId(String keyToCompare) {
    String ownKey = getAutogeneratedId();
    if (keyToCompare.length() < ownKey.length()) {
      return false;
    }
    if (!keyToCompare.substring(0, ownKey.length()).equals(ownKey)) {
      return false;
    }
    if (keyToCompare.length() == ownKey.length()) {
      return true;
    }
    if (keyToCompare.substring(ownKey.length()).matches("^_[0-9]*$")) {
      return true;
    }
    return false;
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AsciiDocVisitor) {
      ((AsciiDocVisitor) visitor).visitSections(this);
      return;
    }

    super.accept(visitor);
  }

  private static String trimHeading(String text) {
    if (text.charAt(0) == '=') {
      // new style heading
      text = StringUtil.trimLeading(text, '=').trim();
    } else if (text.charAt(0) == '#') {
      // markdown style heading
      text = StringUtil.trimLeading(text, '#').trim();
    } else {
      // old style heading
      text = text.replaceAll("[-=~^+\n \t]*$", "");
    }
    return text;
  }

  @Override
  public String getName() {
    return getTitle();
  }

  @Override
  public ItemPresentation getPresentation() {
    return AsciiDocPsiImplUtil.getPresentation(this);
  }

  @Override
  public Icon getIcon(int ignored) {
    return AsciiDocIcons.Structure.SECTION;
  }

  @NotNull
  @Override
  public String getDescription() {
    return getTitle();
  }

  @NotNull
  @Override
  public String getFoldedSummary() {
    ASTNode heading = getNode().findChildByType(AsciiDocTokenTypes.HEADING);
    if (heading == null) {
      throw new IllegalStateException("heading without heading");
    }
    return heading.getText();
  }
}
