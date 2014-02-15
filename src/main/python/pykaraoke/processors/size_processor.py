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


class BasePositionProcessor(Processor):

    def _calculate_space_correction(self, obj, ctx, octx):
        space_correction = 0
        space_correction += ctx.space_widths[octx.style] * (len(obj.text) - len(obj.text.rstrip()))
        space_correction -= ctx.space_widths[octx.style] * (len(obj.text) - len(obj.text.lstrip()))

        octx.space_correction = space_correction

    def _calculate_top_left(self, obj, ctx, octx):
        if octx.anchor in (7, 8, 9):
            obj["top"] = octx.margin[2]
            octx.anchor_y = "top"
        elif octx.anchor in (4, 5, 6):
            obj["top"] = (ctx.resolution[1]/2) - (obj["height"]/2)
            octx.anchor_y = "middle"
        elif octx.anchor in (1, 2, 3):
            obj["top"] = ctx.resolution[1] - obj["height"] - octx.margin[2]
            octx.anchor_y = "bottom"

        if octx.anchor in (1, 4, 7):
            obj["left"] = obj.margin[0]
            octx.anchor_x = "left"
        elif octx.anchor in (2, 5, 8):
            obj["left"] = (ctx.resolution[0]/2) - (obj["width"]/2)
            octx.anchor_x = "center"
        elif octx.anchor in (3, 6, 9):
            obj["left"] = ctx.resolution[0] - obj["width"] - octx.margin[1]
            octx.anchor_x = "right"

        obj["left"] -= octx.space_correction

    def _calculate_points(self, obj, octx):
        obj["middle"] = obj["top"] + obj["height"]/2
        obj["bottom"] = obj["top"] + obj["height"]
        obj["y"] = obj[octx.anchor_y]

        obj["center"] = obj["left"] + (obj["width"] - octx.space_correction)/2
        obj["right"] = obj["left"] + (obj["width"] - octx.space_correction)
        obj["x"] = obj[octx.anchor_x]


class DefaultPositionProcessor(BasePositionProcessor):
    """
    The default position processor.
    """

    def _pre_process(self, lines, ctx):
        ctx.resolution = get_viewport().resolution
        ctx.space_widths = {}
        ctx.linectx = {}

        for line in lines:
            if line.style not in ctx.space_widths:
                ctx.space_widths[line.style] = line.style.text_extents(" ")["width"]
            ctx.linectx[line] = ProcessingContext()
            ctx.linectx[line].syllables = {}

            lctx = ctx.linectx[line]
            lctx.anchor = line.anchor
            lctx.margin = line.margin
            lctx.style = line.style

            for syllable in line.syllables:
                ctx.linectx[line].syllables[syllable] = ProcessingContext()

    def _process(self, lines, ctx):
        for line in lines:
            lctx = ctx.linectx[line]


            self._calculate_space_correction(line, ctx, lctx)
            self._calculate_top_left(line, ctx, lctx)
            self._calculate_points(line, lctx)

            pos = line["left"]
            for syllable in line.syllables:
                sctx = lctx.syllables[syllable]
                sctx.anchor = line.anchor
                sctx.margin = line.margin
                sctx.style = line.style
                sctx.anchor_x, sctx.anchor_y = lctx.anchor_x, lctx.anchor_y
                self._calculate_space_correction(syllable, ctx, sctx)

                syllable["top"] = line["top"]
                syllable["left"] = pos

                self._calculate_points(syllable, sctx)

                pos += syllable["width"]


class KanjiPositionProcessor(BasePositionProcessor):
    """
    Processor for Kanjis.
    """

    def _pre_process(self, lines, ctx):
        ctx.resolution = get_viewport().resolution
        ctx.linectx = {}

        for line in lines:
            ctx.linectx[line] = ProcessingContext()
            ctx.linectx[line].syllables = {}

            lctx = ctx.linectx[line]
            lctx.anchor = line.anchor
            lctx.margin = line.margin
            lctx.style = line.style

            for syllable in line.syllables:
                ctx.linectx[line].syllables[syllable] = ProcessingContext()

    def _process(self, lines, ctx):
        for line in lines:
            height = 0
            max_width = 0

            last_leading = 0
            first = True
            for syllable in line.syllables:
                if first and len(syllable.text.strip()) == 0:
                    continue
                else:
                    first = False

                if "intlead" in syllable:
                    height += syllable["intlead"]

                height += syllable["height"]

                if "extlead" in syllable:
                    height += syllable["extlead"]
                    last_leading = syllable["extlead"]

                max_width = max(max_width, syllable["width"])

                sctx = ctx.linectx[line].syllables[syllable]


            height -= last_leading

            line["height"] = height
            line["width"] = max_width

            ctx.linectx[line].space_correction = 0

            self._calculate_top_left(line, ctx, ctx.linectx[line])
            self._calculate_points(line, ctx.linectx[line])

            pos = line["top"]
            first = True
            for syllable in line.syllables:
                if first and len(syllable.text.strip()) == 0:
                    empty = True
                else:
                    empty = False
                    first = False

                sctx = ctx.linectx[line].syllables[syllable]
                sctx.anchor = line.anchor
                sctx.margin = line.margin
                sctx.style = line.style
                sctx.width = line.style.text_extents(syllable.text.strip())["width"]
                sctx.space_correction = 0
                sctx.anchor_x, sctx.anchor_y = ctx.linectx[line].anchor_x, ctx.linectx[line].anchor_y

                if "intlead" in syllable and not empty:
                    pos += syllable["intlead"]

                syllable["top"] = pos
                syllable["left"] = line["left"] + (max_width/2) - (sctx.width/2)

                if not empty:
                    pos+= syllable["height"]
                    if "extlead" in syllable:
                        pos += syllable["extlead"]

                self._calculate_points(syllable, sctx)


class PositionProcessor(MultiProcessor):

    def __init__(self, kanji_alignments=(1, 4, 7, 3, 6, 9)):
        MultiProcessor.__init__(self, (DefaultPositionProcessor, KanjiPositionProcessor), None)
        self.kanji_alignments = kanji_alignments

    def _select(self, processor, line, ctx):
        return isinstance(
            processor,
            (KanjiPositionProcessor if line.anchor in self.kanji_alignments else DefaultPositionProcessor)
        )
