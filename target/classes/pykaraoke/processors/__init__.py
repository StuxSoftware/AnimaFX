"""
Contains useful processors.
"""
__author__ = 'StuxCrystal'

from base import ProcessingContext, Processor
from size_processor import SizeProcessor, PositionProcessor
from timing_processor import FadeTimingProcessor
from index_processor import IndexProcessor, CountProcessor, LinkingProcessor

__all__ = [
    "Processor", "ProcessingContext",
    "SizeProcessor", "PositionProcessor",
    "FadeTimingProcessor",
    "process"
]


def process(lines, *processors):
    """
    Processes all lines.
    """
    # Go through all processors.
    for processor in processors:

        # If the processor has to be instantiated first.
        if callable(processor):
            # Instantiate the processor.
            processor = processor()

        # Process lines.
        processor.process(lines)
