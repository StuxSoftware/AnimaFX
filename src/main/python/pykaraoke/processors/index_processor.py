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
Processors for indexing.
"""
from base import Processor
__author__ = 'StuxCrystal'


class IndexProcessor(Processor):
    """
    Indexes each line and syllable.
    """

    def _process(self, lines, ctx):
        for line_index, line in enumerate(lines):
            line["index"] = line_index

            for syllable_index, syllable in enumerate(line.syllables):
                syllable["index"] = syllable_index
                syllable["line_index"] = line_index


class CountProcessor(Processor):
    """
    Adds the count of each object to the processor.
    """

    def _process(self, lines, ctx):
        cLines = len(lines)
        for line in lines:
            line["count"] = cLines

            cSyllables = len(line.syllables)
            for syllable in line.syllables:
                syllable["count"] = cSyllables


class LinkingProcessor(Processor):
    """
    Links each line and each syllable together.
    """

    def _process(self, lines, ctx):
        last_line = None
        for line in lines:
            # Link the lines together.
            line["previous"] = last_line
            if last_line is not None:
                last_line["next"] = line
            last_line = line

            # Do the same for each syllable.
            lastSyllable = None
            for syllable in line.syllables:
                syllable["previous"] = lastSyllable
                if lastSyllable is not None:
                    lastSyllable["next"] = line

            # Set the next-value explicitly for the last syllable.
            if lastSyllable is not None:
                lastSyllable["next"] = None

        # Set the next-value explicitly for the last line.
        if last_line is not None:
            last_line["next"] = None