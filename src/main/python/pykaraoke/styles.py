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
from utils import cache
from environment import get_environment

__author__ = 'StuxCrystal'
__all__ = ["StyleManager"]

class Style(object):
    """
    Represents a simple style.
    """

    def __init__(self, name, font, size, italic, bold, spacing=0):
        """
        Creates a new style object.
        """
        self.name = name
        self.font = font
        self.size = size
        self.italic = italic
        self.bold = bold
        self.spacing = spacing

    def text_extents(self, text):
        """
        Returns the text-extents of the given style.
        """
        environment = get_environment()

        # If the environment does support styles,
        # check if the meta-data equals each other and if so, let the environment calculate
        # the values using the name of the style only.
        if environment.styles_supported:
            orig = Style.get_style(self.name)

            # Check if the styles equal each other.
            if orig is not None and self == orig:
                # If so, only use the style name.
                return environment.text_extents(self.name, text)

        # Use a dictionary if styles are not supported or
        return environment.text_extents(self._to_dict(), text)

    def copy(self):
        """
        Copies the style.
        """
        return Style(**self._to_dict())

    def __repr__(self):
        return "<Style '%s' font:'%s' size:%d bold:%r italic:%r>" % (self.name, self.font, self.size, self.bold, self.italic)

    def _to_dict(self):
        """
        Transforms the style to the internal dictionary type of the style.
        """
        return {"name": self.name, "font": self.font, "size": self.size, "bold": self.bold, "italic": self.italic}

    def __eq__(self, other):
        """
        Checks if both styles are equal.
        """
        if other is None or not isinstance(other, Style):
            return False
        return self._to_dict() == other._to_dict()

    @staticmethod
    def get_all_styles():
        """
        Returns all styles inside the environment. If the environment
        does not support styles, the styles in the document are used.
        """
        return StyleManager.resolve().get_styles()

    @staticmethod
    def get_style(name):
        """
        Returns the style of the environment.
        If the environment does not support styles, the first style with the same name
        in the input document will be used.
        """
        return StyleManager.resolve().get_style(name)

    @staticmethod
    def _from_dict(dict, name=None):
        # If the dict is None, return None.
        if dict is None:
            return None

        # If the name is not in the dict add the name to the dict.
        # Also makes sure that name is not passed twice.
        if name is not None:
            dict["name"] = name
        return Style(**dict)


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
        if environment.styles_supported:
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
        return tuple((Style._from_dict(data, name) for name, data in get_environment().get_styles().items()))


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