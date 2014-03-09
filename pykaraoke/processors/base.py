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
            ["%s:%r" % (key, value) for key, value in object.__getattribute__(
                self, "data"
            ).items()]
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

        Use "ctx" to store temporary values that should be shipped across the
        processing steps.
        """
        pass

    def _process(self, lines, ctx):
        """
        Processes the lines.

        Usually "ctx" will contains data that are calculated during
        pre-processing state.
        """
        pass

    def _post_process(self, lines, ctx):
        """
        Called after the lines are processed by the pre-processor.

        Use this to clean up variables that depend on system resources.
        """
        pass


