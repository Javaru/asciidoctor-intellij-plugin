package org.asciidoc.intellij.actions.asciidoc;

import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.psi.PsiFile;
import org.asciidoc.intellij.actions.AsciiDocActionUtil;
import org.asciidoc.intellij.file.AsciiDocFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actions.AbstractToggleUseSoftWrapsAction;
import com.intellij.openapi.editor.impl.softwrap.SoftWrapAppliancePlaces;



public class ToggleSoftWrapsAction extends AbstractToggleUseSoftWrapsAction {
  public ToggleSoftWrapsAction() {
    super(SoftWrapAppliancePlaces.MAIN_EDITOR, false);
    copyFrom(ActionManager.getInstance().getAction(IdeActions.ACTION_EDITOR_USE_SOFT_WRAPS));
  }

  @Override
  public void update(@NotNull AnActionEvent event) {
    PsiFile file = event.getData(LangDataKeys.PSI_FILE);
    boolean enabled = false;
    if (file != null) {
      for (String ext : AsciiDocFileType.DEFAULT_ASSOCIATED_EXTENSIONS) {
        if (file.getName().endsWith("." + ext)) {
          enabled = true;
          break;
        }
      }
    }
    event.getPresentation().setEnabledAndVisible(enabled);
  }

  @Nullable
  @Override
  protected Editor getEditor(@NotNull AnActionEvent e) {
    return AsciiDocActionUtil.findAsciiDocTextEditor(e);
  }
}
