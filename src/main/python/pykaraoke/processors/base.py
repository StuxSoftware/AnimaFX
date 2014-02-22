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
Base Class for the Karaoke-Processors.
"""
__author__ = 'StuxCrystal'


class ProcessingContext(object):
    """
    This is just a context for processing purposes.
    """
    def __init__(self):
        object.__setattr__(self, "data", {})

    def __getattr__(self, item):
        try:
            return object.__getattribute__(self, "data")[item]
        except:
            raise AttributeError(item)

    def __setattr__(self, key, value):
        object.__getattribute__(self, "data")[key] = value

    def __delattr__(self, item):
        try:
            del object.__getattribute__(self, "data")[item]
        except KeyError as e:
            raise AttributeError(item)

    def __contains__(self, item):
        return item in object.__getattribute__(self, "data")

    def __repr__(self):
        return "<PreprocessingContext " + ", ".join(
            ["%s:%r" % (key, value) for key, value in object.__getattribute__(self, "data").items()]
        ) + ">"


class Processor(object):
    """
    The Base-class for the processor.
    """

    def __init__(self):
        """
        Do nothing
        """
        pass

    def process(self, lines):
        """
        Delegates to the processing functions
        """
        ctx = ProcessingContext()
        self._pre_process(lines, ctx)
        self._process(lines, ctx)
        self._post_process(lines, ctx)

    def _pre_process(self, lines, ctx):
        """
        Called before the processor actually processes the lines.
        Useful if you want to retrieve some statistics about the lines.

        Use "ctx" to store temporary values that should be shipped across the processing steps.
        """
        pass

    def _process(self, lines, ctx):
        """
        Processes the lines.

        Usually "ctx" will contains data that are calculated during pre-processing state.
        """
        pass

    def _post_process(self, lines, ctx):
        """
        Called after the lines are processed by the pre-processor.

        Use this to clean up variables that depend on system resources.
        """
        pass


class MultiProcessor(Processor):
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
        return self.selector(processor, line, ctx)