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
Contains the environment based documents.
"""
__author__ = 'StuxCrystal'

from pykaraoke.core.structures.styles import StyleManager
from pykaraoke.core.environment import get_environment
from pykaraoke.core.structures.lines import Line
from pykaraoke.document.base import Document

__all__ = [
    "EnvironmentDocument",
    "InputDocument", "OutputDocument"
]


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
        self.lines = Line.parse_lines(self.get_environment().get_syllables())

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

    def _support_line_writing(self):
        """
        This is a document to write to the main output of the environment
        """
        return True

    def add_line(self, line):
        self.get_environment().write_line(line)