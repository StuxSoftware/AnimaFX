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
Returns common types of karabuilder.
"""
from pykaraoke.utils import to_ass_time
from pykaraoke.core.structures.styles import StyleManager, Style
from pykaraoke.core.environment import get_environment

__author__ = 'StuxCrystal'

__all__ = ["Style", "Line", "Syllable", "Viewport"]


class Viewport(object):
    """
    Viewports.
    """

    # Singleton instance.
    _viewport = None

    def __new__(cls):
        if Viewport._viewport is not None:
            return Viewport._viewport
        Viewport._viewport = super(Viewport, cls).__new__(cls)
        return Viewport._viewport

    def __init__(self):
        """
        Internal object.
        """
        self.environment = get_environment()

    @property
    def resolution(self):
        """
        Returns the resolution.
        """
        return self.environment.get_viewport_size()

    @property
    def fps(self):
        """
        Returns the current fps or None if fps are not reported by the
        environment.
        """
        if not self.environment.video_info_supported:
            return None
        return self.environment.get_fps()

    @property
    def lines(self):
        """
        Returns a line.
        """
        return Line.get_lines()

    @property
    def styles(self):
        """
        Returns all styles.
        """
        return StyleManager.resolve().get_styles()


class ExtensibleObject(object):
    """
    Represents a extensible object.
    """

    def __init__(self):
        self.extension = {}

    def __getitem__(self, item):
        return self.extension[item]

    def __setitem__(self, key, value):
        self.extension[key] = value

    def __delitem__(self, key):
        del self.extension[key]

    def __contains__(self, item):
        return item in self.extension

    def _copy(self):
        return self.extension.copy()


class Syllable(ExtensibleObject):
    """
    Stores the data of the syllable.
    """

    def __init__(self, line, start, duration, text):
        """
        Creates a new syllable.
        """
        ExtensibleObject.__init__(self)
        self.line = line
        self.start = start
        self.duration = duration
        self.text = text

    def copy(self):
        result = Syllable(self.line, self.start, self.duration, self.text)
        result.extension = self._copy()
        return result

    def __repr__(self):
        return "<Syllable start:%d duration:%d text:'%s' extension:%s>" % (
            self.start, self.duration, self.text, self.extension
        )

    @property
    def rel_start(self):
        """
        Returns the relative start time.
        """
        return self.start - self.line.start

    @property
    def rel_end(self):
        return self.rel_start + self.duration

    @property
    def end(self):
        return self.start + self.duration*10

    @end.setter
    def end(self, value):
        if value < self.start:
            raise ValueError("End may not be sooner than start.")

        self.duration = (value-self.start)/10


class Line(ExtensibleObject):
    """
    Stores the data of the syllable.
    """

    def __init__(self, start, end, style, anchor, margin=(10, 10, 10),
                 layer=0, text=None, extensions=None):
        """
        Creates a new line.
        """
        ExtensibleObject.__init__(self)
        self.start = start
        self.end = end
        self.style = self._get_style(style)
        self.margin = margin
        self.anchor = anchor
        self.layer = layer
        self.raw_text = text
        self.syllables = []
        self.extension = extensions if extensions is not None else {}

    def write(self):
        get_environment().write_line(self)

    def copy(self):
        result = Line(
            self.start, self.end, self.style.copy(),
            self.anchor, self.margin, self.layer,
            extensions=self.extension.copy()
        )
        result.syllables = [syllable.copy() for syllable in self.syllables]
        result.raw_text = self.raw_text
        return result

    @property
    def duration(self):
        """
        Sets the duration.
        """
        return self.end - self.start

    @property
    def text(self):
        if self.raw_text is not None:
            return self.raw_text

        return "".join([syllable.text for syllable in self.syllables])

    @text.setter
    def text(self, value):
        self.raw_text = value

    def __repr__(self):
        return "<Line start:%s end:%s style:%r margin:%s " \
               "anchor:%d %s extensions:%r layer:%i>" % (
            to_ass_time(self.start), to_ass_time(self.end),
            self.style, repr(self.margin), self.anchor,
            (
                ("syllables:%s" % repr(self.syllables))
                if self.raw_text is None else ("text:'%s'" % self.raw_text)
            ),
            self.extension, self.layer
        )

    @staticmethod
    def _get_style(style):
        """
        Returns the style.
        """
        if isinstance(style, Style):
            return style

        if isinstance(style, str):
            return Style.get_style(style)
        return Style(**style)

    @staticmethod
    def get_lines():
        """
        Returns all lines.
        """
        environment = get_environment()

        result = []
        for rawline in environment.get_syllables():
            line = Line(**rawline[0])
            for syllable in rawline[1:]:
                line.syllables.append(Syllable(line=line, **syllable))
            result.append(line)
        return result