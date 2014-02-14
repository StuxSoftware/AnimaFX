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
        lastLine = None
        for line in lines:
            # Link the lines together.
            line["previous"] = lastLine
            if lastLine is not None:
                lastLine["next"] = line
            lastLine = line

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
        if lastLine is not None:
            lastLine["next"] = None