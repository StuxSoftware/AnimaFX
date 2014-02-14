"""
Karabuilder is the high-level library to generate karaoke effects using
any environment that is capable of using "TextExtents".
"""
__author__ = 'StuxCrystal'

from types import Style, Line, Syllable, get_viewport

from environment import get_environment, set_environment
from environment import Environment
from environment import EnvironmentRedefinitionException, UnsupportedOperationException

from document import Document, EnvironmentDocument, InputDocument, OutputDocument, LineBuffer