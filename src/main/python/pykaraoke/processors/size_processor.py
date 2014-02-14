#
# The MIT License (MIT)
#
# Copyright (c) 2014 StuxCrystal
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all
# copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
# SOFTWARE.
#

"""
Contains processors for sizes and placements.
"""
from pykaraoke.types import get_viewport
from base import ProcessingContext, Processor, MultiProcessor

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


class DefaultPositionProcessor(Processor):
    """
    The default position processor.
    """

    def _pre_process(self, lines, ctx):
        ctx.resolution = get_viewport().resolution
        ctx.space_widths = {}
        ctx.linectx = {}

        for line in lines:
            if line.style not in ctx.space_widths:
                ctx.space_widths[line.style] = line.style.text_extents(line.style, " ")
            ctx.linectx[line] = ProcessingContext()
            ctx.linectx[line].syllables = {}

            for syllable in line.syllables:
                ctx.linectx[line].syllables[syllable] = ProcessingContext()

    def _process(self, lines, ctx):

        for line in lines:
            lctx = ctx.linectx[line]
            lctx.anchor = line.anchor
            lctx.margin = line.margin

            self._calculate_top_left(line, ctx, lctx)
            self._calculate_points(line, lctx)

            pos = line["left"]
            for syllable in line.syllables:
                sctx = lctx.syllables[syllable]
                sctx.anchor = line.anchor
                sctx.margin = line.margin

                syllable["top"] = line["top"]
                syllable["left"] = pos

                self._calculate_points(syllable, sctx)

                pos += syllable["width"]

    def _calculate_top_left(self, obj, ctx, octx):

        if octx.anchor in (7, 8, 9):
            obj["top"] = octx.margin[2]
            octx.anchor_y = "top"
        elif octx.anchor in (4, 5, 6):
            obj["top"] = ctx.resolution[1]/2 - obj["height"]/2
            octx.anchor_y = "middle"
        elif octx.anchorin (1, 2, 3):
            obj["top"] = ctx.resolution[1] - obj["height"] - octx.margin[2]
            octx.anchor_y = "bottom"

        if octx.anchor in (1, 4, 7):
            obj["left"] = obj.margin[0]
            octx.anchor_x = "left"
        elif octx.anchor in (2, 5, 8):
            obj["left"] = ctx.resolution[0]/2 - obj["width"]/2
            octx.anchor_x = "center"
        elif octx.anchor in (3, 6, 9):
            obj["left"] = ctx.resolution[0] - obj["width"] - octx.margin[1]
            octx.anchor_x = "right"

        space_correction = 0
        space_correction -= ctx.space_widths[obj.style] * (len(obj) - obj(obj.rstrip()))
        space_correction += ctx.space_widths[obj.style] * (len(obj) - obj(obj.lstrip()))

        octx.space_correction = space_correction

        obj["left"] -= space_correction

    def _calculate_points(self, obj, octx):
        obj["middle"] = obj["top"] + obj["height"]/2
        obj["bottom"] = obj["top"] + obj["height"]
        obj["x"] = obj[octx.anchor_y]

        obj["center"] = obj["left"] + (obj["width"] - octx.space_correction)/2
        obj["right"] = obj["left"] + (obj["width"] - octx.space_correction)
        obj["y"] = obj[octx.anchor_x]


PositionProcessor = DefaultPositionProcessor

## class PositionProcessor(MultiProcessor):
##     pass