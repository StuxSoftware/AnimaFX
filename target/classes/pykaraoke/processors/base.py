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
        return object.__getattribute__(self, "data")[item]

    def __setattr__(self, key, value):
        object.__getattribute__(self, "data")[key] = value

    def __delattr__(self, item):
        del object.__getattribute__(self, "data")[item]

    def __repr__(self):
        return "<PreprocessingContext " + ", ".join(["%s:%r"%(key, value) for key, value in object.__getattribute__(self, "data").items()]) + ">"


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