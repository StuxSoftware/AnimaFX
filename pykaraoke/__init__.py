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
Karabuilder is the high-level library to generate karaoke effects using
any environment that is capable of using "TextExtents".
"""
__author__ = 'StuxCrystal'

__all__ = [
    "StyleManager",
    "Style", "Line", "Syllable", "Viewport",
    "get_environment", "set_environment",
    "Environment",
    "EnvironmentRedefinitionException", "UnsupportedOperationException",
    "Document", "EnvironmentDocument", "InputDocument", "OutputDocument", "LineBuffer",
    "Image", "Particle",
    "Time"
]

from .styles import StyleManager
from .structures import Style, Line, Syllable, Viewport

from .environment import get_environment, set_environment
from .environment import Environment
from .environment import EnvironmentRedefinitionException, UnsupportedOperationException

from .document import Document, EnvironmentDocument, InputDocument, OutputDocument, LineBuffer

from .images import Image
from .particles import Particle
from .utils import Time
