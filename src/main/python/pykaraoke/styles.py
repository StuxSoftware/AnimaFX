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
from structures import Style
from utils import cache
from environment import get_environment

__author__ = 'StuxCrystal'


class StyleManager(object):
    """
    Manager for styles. This class is abstract.
    Generate the apropriate style manager using
    >>> StyleManager.resolve()
    """

    # Contains the current style manager.
    manager = None

    def _get_style(self, name):
        """
        Returns the style with the given name. Internal function.
        """
        raise NotImplementedError

    def get_style(self, name):
        """
        Returns a copy of the style with the given name.
        """
        return self._get_style(name).copy()
    __getitem__ = get_style

    def _get_styles(self):
        """
        Returns all styles. Internal function.
        """
        raise NotImplementedError

    def get_styles(self):
        """
        Returns an iterator over all styles.
        """
        return (style.copy() for style in self._get_styles())
    __iter__ = get_styles

    @staticmethod
    @cache
    def resolve():
        """
        Returns the style manager for this environment.
        """
        environment = get_environment()

        # Check if the environment provides its own
        # style manager.
        smgr = environment.stylemanager
        if smgr is not None:
            return smgr

        # Choose the right pre-packaged style manager
        # if not.
        if environment.supports_styles:
            return EnvironmentStyleManager()
        else:
            return CachedStyleManager()


class EnvironmentStyleManager(StyleManager):
    """
    Retrieves all styles from the environment.
    """

    def get_style(self, name):
        return Style._from_dict(get_environment().get_style(name), name)

    def get_styles(self):
        return tuple((Style._from_dict(data, name) for data, name in get_environment().get_styles().items()))


class CachedStyleManager(StyleManager):
    """
    Caches all styles from the line. If a two styles with different attributes
    but same name are detected, the style that was found first will be used.

    Styles that have no name will be ignored.
    """

    def __init__(self):
        self.styles = {}
        self._parse_styles(get_environment().get_syllables())

    def _parse_styles(self, lines):
        for line in lines:
            cur_style = line["style"]
            if "name" not in cur_style:
                continue

            if cur_style["name"] in self.styles:
                continue

            self.styles[cur_style["name"]] = Style._from_dict(cur_style)

    def _get_style(self, name):
        return self.styles[name]

    def _get_styles(self):
        return self.styles.values()