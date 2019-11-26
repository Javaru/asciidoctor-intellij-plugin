package org.asciidoc.intellij.parser;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import org.asciidoc.intellij.AsciiDocLanguage;
import org.asciidoc.intellij.lexer.AsciiDocElementType;

/**
 * @author yole
 */
public interface AsciiDocElementTypes {
  IFileElementType FILE = new IFileElementType(AsciiDocLanguage.INSTANCE);
  IElementType SECTION = new AsciiDocElementType("SECTION");
  IElementType BLOCK_MACRO = new AsciiDocElementType("BLOCK_MACRO");
  IElementType INLINE_MACRO = new AsciiDocElementType("INLINE_MACRO");
  IElementType BLOCK = new AsciiDocElementType("BLOCK");
  IElementType BLOCK_ATTRIBUTES = new AsciiDocElementType("BLOCK_ATTRIBUTES");
  IElementType BLOCKID = new AsciiDocElementType("BLOCKID");
  IElementType REF = new AsciiDocElementType("REF");
  IElementType LISTING = new AsciiDocElementType("LISTING");
  IElementType LINK = new AsciiDocElementType("LINK");
  IElementType ATTRIBUTE_DECLARATION = new AsciiDocElementType("ATTRIBUTE_DECLARATION");
  IElementType ATTRIBUTE_REF = new AsciiDocElementType("ATTRIBUTE_REF");
  IElementType ATTRIBUTE_DECLARATION_NAME = new AsciiDocElementType("ATTRIBUTE_DECLARATION_NAME");
  IElementType URL = new AsciiDocElementType("URL");
  IElementType TITLE = new AsciiDocElementType("TITLE");
}
