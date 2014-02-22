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
Represents the input and the output document.
"""
from styles import StyleManager
from operations import set_text, set_value, set_extension, retime
from processors import process as process_lines
from environment import get_environment
from types import Style, Line
__author__ = 'StuxCrystal'


__all__ = [
    "Document",
    "LineBuffer"
    "EnvironmentDocument", "InputDocument", "OutputDocument"
]


class Document(object):
    """
    Represents a simple document.

    Reading document objects are like builders:
    Non-Terminal Operations return itself.
    """

    def get_styles(self):
        """
        Returns the styles of the document.
        """
        raise NotImplementedError

    def get_style(self, name):
        """
        Returns the style of the document with the given name.
        """
        raise NotImplementedError

    def add_lines(self, *lines):
        """
        Adds all lines...
        """
        for line in lines:
            self._add_line(line)

    def _add_line(self, line):
        """
        Adds a line to the document.
        """
        raise NotImplementedError

    def get_lines(self):
        """
        Returns all lines of the document.
        """
        return [line.copy() for line in self._get_lines()]

    def _get_lines(self):
        """
        Returns all lines of the document.
        """
        raise NotImplementedError

    def process(self, *processors):
        """
        Processes all lines in the document with the given processors.
        """
        if not self.supports_line_reading:
            raise NotImplementedError

        process_lines(self._get_lines(), *processors)
        return self

    def annotate(self, func):
        """
        Executes the given function on all lines.
        """
        if not callable(func):
            raise ValueError("Cannot execute the function...")

        for line in self._get_lines():
            func(line)

        return self

    def set_text(self, func):
        """
        Sets the text of the lines.
        """
        return self.annotate(set_text(func))

    def set_value(self, name, func):
        """
        Sets a value of the line.
        """
        return self.annotate(set_value(name, func))

    def set_extension(self, func):
        """
        Sets a extension function.

        The function returns a tuple (name, value).
        """
        return self.annotate(set_extension(func))

    def retime(self, func):
        """
        Retimes the line. The result of the transformer is a tuple (absolute:bool, new_start:int, new_end:int)
        If new_start or new_end are None, they are ignored.
        """
        return self.annotate(retime(func))

    def refactor(self, func):
        """
        Refactors each line. e.g makes multiple lines using it.
        Will create a buffer that will be returned.

        The line passed to the function is a copy of the underlying line.
        """
        result = []

        for line in self.get_lines():
            refactor_result = func(line)
            if isinstance(refactor_result, Line):
                result.append(refactor_result)
            else:
                result.extend(refactor_result)

        return LineBuffer(result)

    def syllables(self):
        """
        Makes a syllable wise effect out of them.
        """
        return self.refactor(lambda line: [
            Line(
                syllable.start, syllable.end, line.style, line.anchor, extensions={"syllable": syllable, "line": line},
                text=syllable.text
            )
            for syllable in line.syllables
        ])

    def stream_to(self, document):
        """
        Streams the contents to the specified document.

        Returns the given document.
        """
        if not self.supports_line_reading:
            raise ValueError("Unsupported Operation on non-reading objects.")

        if not document.supports_line_writing:
            raise ValueError("The document does not support writing")

        document.add_lines(*self.get_lines())
        return document

    def stream_from(self, document):
        """
        Short for
        >>> document.stream_to(self)
        """
        document.stream_to(self)
        return self

    def _support_line_reading(self):
        """
        Internal function: Return true if reading from this document is supported.
        """
        return False

    def _support_line_writing(self):
        """
        Internal function: Return true if writing to this document is supported.
        """
        return False

    def _support_styles(self):
        """
        Internal function: Return true if styles are supported in this document.
        """
        return False

    @property
    def supports_line_reading(self):
        """
        Checks if you can read lines from the document.
        """
        return self._support_line_reading()

    @property
    def supports_line_writing(self):
        """
        Checks if you can write lines to this document
        """
        return self._support_line_writing()

    @property
    def supports_styles(self):
        """
        Checks if this document supports styles.
        """
        return self._support_styles()

    def __iter__(self):
        """
        Iterates over all lines
        """
        if not self._support_line_reading():
            return []
        return self.get_lines()


class LineBuffer(Document):
    """
    A buffer for lines.
    """

    def __init__(self, lines=[]):
        self.lines = lines

    def _support_line_writing(self):
        return True

    def _support_line_reading(self):
        return True

    def _get_lines(self):
        return self.lines

    def _add_line(self, line):
        self.lines.append(line)

    def clear(self):
        """
        Clears the buffer.

        Terminal operation.
        """
        self.lines = []


class EnvironmentDocument(Document):
    """
    Base-Class for Documents using the environment.
    """

    def get_style_manager(self):
        """
        Returns the style manager of the environment.
        """
        return StyleManager.resolve()

    def get_environment(self):
        """
        Returns the environment the software is running under.
        """
        return get_environment()


class InputDocument(EnvironmentDocument):
    """
    Represents a document where you can read the lines from.
    """

    def __init__(self):
        self.lines = Line.get_lines()

    def _support_line_reading(self):
        """
        This document supports reading
        """
        return True

    def _support_styles(self):
        """
        This document supports styles if the environment supports styles.
        """
        return True

    def _get_lines(self):
        """
        Returns a copy of all lines.
        """
        return self.lines

    def get_styles(self):
        """
        Returns all styles if they are supported.
        """
        return StyleManager.resolve().get_styles()

    def get_style(self, name):
        """
        Returns the style with the given name.
        """
        return StyleManager.resolve().get_style(name)


class OutputDocument(EnvironmentDocument):
    """
    Represents a document for the output.
    """

    def __init__(self):
        self.environment = get_environment()

    def _support_line_writing(self):
        """
        This is a document to write to the main output of the environment
        """
        return True

    def _add_line(self, line):
        self.environment.write_line(line)