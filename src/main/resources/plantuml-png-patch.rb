require 'asciidoctor/extensions'
require 'asciidoctor-diagram/plantuml/extension'

include ::Asciidoctor

# The JavaFX preview doesn't support SVG well. Therefore we'll render all PlantUML images as PNG

module SvgToPngHack
  # see https://github.com/asciidoctor/asciidoctor-diagram/blob/master/lib/asciidoctor-diagram/extensions.rb for the souce
  def process(parent, reader_or_target, attributes)
    # render as SVG to be used for save-as functionality
    attributes['format'] = 'svg'
    # rewind figure-number for second call (note that it will only be incremented if the diagram has a title)
    fignum = parent.document.attr('figure-number')
    super(parent, reader_or_target, attributes)
    parent.document.set_attr('figure-number', fignum)
    # render a second time for PNG to be used in preview
    attributes['format'] = 'png'
    super(parent, reader_or_target, attributes)
  end
end

class Diagram::PlantUmlBlockProcessor
  prepend SvgToPngHack
end

class Diagram::PlantUmlBlockMacroProcessor
  prepend SvgToPngHack
end

class Diagram::SaltBlockProcessor
  prepend SvgToPngHack
end

class Diagram::SaltBlockMacroProcessor
  prepend SvgToPngHack
end
