"""
The processors for timings.
"""
from base import Processor

__author__ = 'StuxCrystal'


class FadeTimingProcessor(Processor):
    """
    Calculates the infade and outfade for each line.
    """

    def _process(self, lines, ctx):
        """
        Adds the data.
        """
        line_before = None
        for line in lines:
            if line_before is None:
                line["infade"] = 0
                line_before = line
                continue

            line["infade"] = line_before["outfade"] = line.start - line_before.end
            line_before = line

        if line_before is not None:
            line_before["outfade"] = 0