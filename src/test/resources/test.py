from pykaraoke.processors import SizeProcessor, PositionProcessor
from pykaraoke.document import InputDocument, OutputDocument

# Makes a syllable-wise karaoke
(
    InputDocument()
    .process(SizeProcessor, PositionProcessor)
    .syllables()
    .set_text(lambda line: "{\\pos(%.2f,%.2f)}{%.2f,%.2f}%s" % (
        line["syllable"]["x"], line["syllable"]["y"],
        line["syllable"]["width"], line["syllable"]["height"],
        line.text)
    )
    .retime(lambda line: (True, line["line"].start, line["line"].end))
    .stream_to(OutputDocument())
)

