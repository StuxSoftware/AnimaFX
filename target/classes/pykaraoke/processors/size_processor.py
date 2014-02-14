"""
Contains processors for sizes and placements.
"""
from pykaraoke import get_viewport
from base import Processor, ProcessingContext

__author__ = 'StuxCrystal'


class SizeProcessor(Processor):
    """
    Executes text-extents on all syllables and lines.
    """

    def _process(self, lines, ctx):
        """
        Calculates the sizes of each syllables and each line.
        """
        for line in lines:
            self._register_extents(line, line.style.text_extents(line.text))

            for syl in line.syllables:
                self._register_extents(syl, line.style.text_extents(syl.text))

    def _register_extents(self, obj, extents):
        """
        Registers the extents.
        """
        # Adds basic objects.
        obj["width"], obj["height"] = extents["width"], extents["height"]

        self._register_optional_value(obj, extents, "ascent")         # Ascent
        self._register_optional_value(obj, extents, "descent")        # Descent
        self._register_optional_value(obj, extents, "extlead")        # External Leading
        self._register_optional_value(obj, extents, "intlead")        # Internal Leading
        self._register_optional_value(obj, extents, "advance")        # Calculates the advance.

    def _register_optional_value(self, obj, extents, name):
        """
        Registers optional values if they exist.
        """
        if name in extents:
            obj[name] = extents[name]


class PositionProcessor(Processor):
    """
    Calculates the position of each syllable.
    """

    def __init__(self, kanji_anchor=(1, 4, 7, 3, 6, 9)):
        """
        Creates a new position calculator
        """
        super(Processor, self).__init__()
        self.kanji_anchor = kanji_anchor

    def _pre_process(self, lines, ctx):
        """
        Checks if leading is supported.
        """
        # Do nothing if there is no line.
        if len(lines) == 0:
            pass

        # Checks if the sizes are calculated.
        ctx.sizes_calculated = "width" in lines[0] and "height" in lines[0]

        cLine = lines[0]
        if not ctx.sizes_calculated:
            # Execute text-extents to get a dictionary with the ascend etc.
            cLine = lines[0].style.text_extents("Text")

        # Assume all text-extents have the extlead item if the first line has one.
        ctx.leading_support = "extlead" in cLine
        ctx.ascent_support = "ascent" in cLine
        ctx.descent_support = "descent" in cLine
        ctx.advance_support = "advance" in cLine

        # Get viewport resolution
        ctx.viewport_width, ctx.viewport_height = get_viewport().resolution

    def _process(self, lines, ctx):
        """
        Processes the lines and syllables.
        """

        for line in lines:
            lctx = ProcessingContext()
            lctx.style = line.style
            self._extract_extents(line, ctx, lctx)

            if line.anchor in self.kanji_anchor:
                self._kanji_process(line, ctx, lctx)
            else:
                self._default_process(line, ctx, lctx)

    def _extract_extents(self, line, ctx, lctx):
        """
        Extracts the extents.

        Note that the line-context can also be a syllable context.
        Note that the line can also denote a syllable.
        """

        if ctx.sizes_calculated:
            extents = line
        else:
            extents = line.style.text_extents(line.text)

        lctx.width, lctx.height = extents["width"], extents["height"]

        if ctx.leading_support:
            lctx.extlead = extents["extlead"]
        else:
            lctx.extlead = 0

        if ctx.ascent_support:
            lctx.ascent = extents["ascent"]
        else:
            lctx.ascent = 0

        if ctx.descent_support:
            lctx.descent = extents["descent"]
        else:
            lctx.descent = 0

        if hasattr(line, "anchor"):
            lctx.anchor = line.anchor

        lctx.space_width = lctx.style.text_extents(" ")["width"]
        lctx.spaceless_width = lctx.width
        lctx.spaceless_width -= lctx.space_width * (len(line.text) - len(line.text.rstrip()))
        lctx.spaceless_width += lctx.space_width * (len(line.text) - len(line.text.lstrip()))

    def _kanji_process(self, line, ctx, lctx):
        """
        Calculates using kanjis.
        """
        # Returns the width
        lctx.real_width = lctx.width

        # Metadata about the line.
        max_width = 0
        height = 0

        # Calculate maximal width and height.
        for syl in line.syllables:
            sctx = ProcessingContext()
            sctx.style = line.style
            self._extract_extents(syl, ctx, sctx)

            max_width = max(max_width, sctx.width)
            height += sctx.descent + lctx.leading
            if height > 0:
                height += sctx.ascent

        # If the lines are not
        else:
            self._calculate_line_top_left(line, ctx, lctx)
            self._calculate_edge_points(line, lctx)
            return

        height -= sctx.descent

        # Update width and height of the lines.
        if "width" in line:
            line["width"] = max_width

        if "height" in line:
            line["height"] = height

        lctx.width, lctx.height = max_width, height

        # Now it is easy to calculate the top and left of the line.
        self._calculate_line_top_left(line, ctx, lctx)

        # Calculate the
        height = 0
        for syl in line.syllables:
            sctx = ProcessingContext()
            self._extract_extents(syl, ctx, sctx)

            if height > 0:
                height += sctx.ascent
            syl["top"] = height
            height += sctx.descent + lctx.leading

            syl["left"] = line["left"]

            sctx.anchor = line.anchor
            self._calculate_edge_points(syl, sctx)

    def _default_process(self, line, ctx, lctx):
        """
        Calculates the default way.
        """
        # Returns the width
        lctx.real_width = lctx.width

        # Calculate top-left point of the bounding box.
        self._calculate_line_top_left(line, ctx, lctx)
        self._calculate_edge_points(line, lctx)

        pos = line["left"]
        for syl in line.syllables:
            sctx = ProcessingContext()
            sctx.style = line.style
            self._extract_extents(syl, ctx, sctx)
            sctx.real_width = sctx.spaceless_width

            syl["top"] = line["top"]
            syl["left"] = pos

            pos += sctx.width - sctx.extlead

            sctx.anchor = line.anchor
            self._calculate_edge_points(syl, sctx)

    def _calculate_line_top_left(self, line, ctx, lctx):
        if line.anchor in (7, 8, 9):
            line["top"] = line.margin[2]
        elif line.anchor in (4, 5, 6):
            line["top"] = ctx.viewport_height/2 - lctx.height/2
        elif line.anchor in (1, 2, 3):
            line["top"] = ctx.viewport_height - line.margin[2] - lctx.height
        else:
            raise ValueError("Invalid Alignment.")

        if line.anchor in (1, 4, 7):
            line["left"] = line.margin[0]
        elif line.anchor in (2, 5, 8):
            line["left"] = ctx.viewport_width/2 - lctx.width/2
        elif line.anchor in (3, 6, 9):
            line["left"] = ctx.viewport_width - lctx.width - line.margin[1]

    def _calculate_edge_points(self, obj, octx):
        obj["middle"] = obj["top"] + octx.height/2
        obj["bottom"] = obj["top"] + octx.height

        obj["center"] = obj["left"] + octx.real_width/2
        obj["right"] = obj["left"] + octx.real_width

        if octx.anchor in (1, 4, 7):
            obj["x"] = obj["left"]
        elif octx.anchor in (2, 5, 8):
            obj["x"] = obj["center"]
        elif octx.anchor in (3, 6, 9):
            obj["x"] = obj["right"]

        if octx.anchor in (1, 2, 3):
            obj["y"] = obj["bottom"]
        elif octx.anchor in (4, 5, 6):
            obj["y"] = obj["middle"]
        elif octx.anchor in (7, 8, 9):
            obj["y"] = obj["top"]