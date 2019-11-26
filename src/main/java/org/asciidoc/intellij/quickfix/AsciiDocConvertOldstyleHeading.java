package org.asciidoc.intellij.quickfix;

import com.intellij.codeInspection.LocalQuickFixBase;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.asciidoc.intellij.psi.AsciiDocFile;
import org.asciidoc.intellij.psi.AsciiDocSection;
import org.asciidoc.intellij.psi.AsciiDocUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Alexander Schwartz 2016
 */
public class AsciiDocConvertOldstyleHeading extends LocalQuickFixBase {
  public static final String NAME = "Convert to modern AsciiDoc Heading";

  @NotNull
  @Override
  public String getFamilyName() {
    return super.getFamilyName();
  }

  public AsciiDocConvertOldstyleHeading() {
    super(NAME);
  }

  @Override
  @SuppressWarnings("FallThrough")
  public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
    PsiElement element = descriptor.getPsiElement();
    StringBuilder text = new StringBuilder(element.getText());

    if (text.indexOf("\n") == -1) {
      // nothing to fix any more
      return;
    }

    int pos = text.length() - 1;
    while (pos > 0 && (text.charAt(pos) == ' ' || text.charAt(pos) == '\t')) {
      --pos;
    }
    char character = text.charAt(pos);
    int depth = 0;
    switch (character) {
      case '+':
        ++depth;
      case '^':
        ++depth;
      case '~':
        ++depth;
      case '-':
        ++depth;
      case '=':
        ++depth;
        break;
      default:
        return;
    }

    // cut off the second line
    text.replace(text.indexOf("\n"), text.length(), "");

    // prepend right number of equals and a blank
    text.insert(0, " ");
    while (depth > 0) {
      --depth;
      text.insert(0, "=");
    }

    element.replace(createHeading(project, text.toString()));
  }

  @NotNull
  private static PsiElement createHeading(@NotNull Project project, @NotNull String text) {
    AsciiDocFile file = AsciiDocUtil.createFileFromText(project, text);
    AsciiDocSection section = PsiTreeUtil.findChildOfType(file, AsciiDocSection.class);
    Objects.requireNonNull(section, "there should be a section from the text passed as argument");
    return section.getFirstChild();
  }

}
