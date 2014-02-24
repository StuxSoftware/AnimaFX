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
Contains linking processors.
"""
from .base import ProcessingContext, Processor

__author__ = 'StuxCrystal'


class SelectiveProcessor(Processor):
    """
    This is a processor that delegates its objects to
    other processors depending on the properties of the line.

    You have to pass a list of processors and the selector.
    The selector is a function that accepts the processor,
    the line to select and the context for this processor.
    """

    def __init__(self, processors, selector=None):
        super(Processor, self).__init__()
        self.processors = list(processors)
        self.selector = selector

    def _pre_process(self, lines, ctx):
        ctx.contexts = {}

        for index, processor in enumerate(self.processors[:]):
            if callable(processor):
                processor = processor()
                self.processors[index] = processor

            processor_lines = []
            for line in lines:
                if self._select(processor, line, ctx):
                    processor_lines.append(line)

            pctx = ProcessingContext()
            processor._pre_process(processor_lines, pctx)
            ctx.contexts[processor] = (processor_lines, pctx)

    def _process(self, lines, ctx):
        for processor in self.processors:
            processor._process(*ctx.contexts[processor])

    def _post_process(self, lines, ctx):
        for processor in self.processors:
            processor._post_process(*ctx.contexts[processor])

    def _select(self, processor, line, ctx):
        if self.selector is None:
            return True
        return self.selector(processor, line, ctx)