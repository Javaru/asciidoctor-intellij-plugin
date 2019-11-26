package org.asciidoc.intellij.psi;

import org.jetbrains.annotations.NotNull;

public interface AsciiDocSelfDescribe {
  @NotNull
  String getDescription();

  @NotNull
  String getFoldedSummary();
}
