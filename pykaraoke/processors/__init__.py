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
Contains useful processors.
"""
__author__ = 'StuxCrystal'

__all__ = [
    "Processor", "ProcessingContext",
    "SizeProcessor", "PositionProcessor",
    "FadeTimingProcessor",
    "IndexProcessor", "CountProcessor", "LinkingProcessor",
    "MultiProcessor", "BaseProcessor"
    "process"
]

from pykaraoke.processors.base import Processor, ProcessingContext
from pykaraoke.processors.index_processor import IndexProcessor, CountProcessor
from pykaraoke.processors.index_processor import LinkingProcessor
from pykaraoke.processors.timing_processor import FadeTimingProcessor
from pykaraoke.processors.size_processor import SizeProcessor
from pykaraoke.processors.size_processor import PositionProcessor
from pykaraoke.processors.multi_processor import SelectiveProcessor

# Use this "class".
BaseProcessor = lambda: SelectiveProcessor((SizeProcessor, PositionProcessor), None)


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
