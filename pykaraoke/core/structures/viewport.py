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
The viewport information.
"""
__author__ = 'StuxCrystal'

from pykaraoke.core.environment import get_environment
from pykaraoke.core.structures.lines import Line
from pykaraoke.core.structures.styles import StyleManager

__all__ = ["Viewport"]


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
        return Line.parse_lines(self.environment.get_syllables())

    def write(self, line):
        self.environment.write_line(line)

    @property
    def styles(self):
        """
        Returns all styles.
        """
        return StyleManager.resolve().get_styles()

    @property
    def style_manager(self):
        return StyleManager.resolve()
