__author__ = 'stuxcrystal'


from pykaraoke import InputDocument, OutputDocument
from pykaraoke.calc import Vector, bezier, interpolate

(
    # Fetch the lines of the syllable.
    InputDocument()

    # Make a frame for frame fx out of it.
    .frames(fps=(24000/1001), prefer_video=True)

    # Make the lines using the syllable text as text.
    .set_text(lambda line: line["syllable"].text)

    # Start building the ASS-Tags for the Karaoke-FX.
    .build()

    # Change the position
    .tag("pos",

         # Set the origin of the bezier-curve.
         lambda pre, line: Vector(line["syllable"]["x"], line["syllable"]["y"]),

         # Let the syllable move along a bezier curve.
         lambda pre, line: pre + bezier(
             (line["syl_index"]/line["syl_count"]),

             # This are the control points of the curve.
             (0, 20), (20, 20), (20, 40)
         )
    )

    # Set the color.
    .tag("1c", "&HFF00FF&")

    # Set the alpha value.
    .tag("1a", lambda pre, line: interpolate(
            (line["syl_index"]/line["syl_count"]),
            "&H00&", "&HFF&"
    ))

    # Generate the effect.
    .generate()

    # Stream the generated effect into the
    # output document.
    .stream_to(OutputDocument())
)
