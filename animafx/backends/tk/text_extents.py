#
# AnimaFX
# Copyright (C) 2014 StuxCrystal
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#
"""
Calculates the text-extents using Tkinter.
"""

from functools import lru_cache

from ..base import TextExtentBackend

__author__ = 'StuxCrystal'
__all__ = ["TkinterFontBackend"]

# Test if Tkinter is supported by the interpreter.
try:
    # Check if _tkinter is known.
    import _tkinter

    # Check if tkinter.font can be imported.
    from tkinter.font import Font

# If not tell the backend manager to ignore this backend.
except ImportError as e:
    TkinterFontBackend = None

# Implement the backend if supported.
else:
    del _tkinter
    from .console_tk import InternalWindow

    # Apply scaling to the font.
    FONT_SCALING = 64

    class TkinterFontBackend(TextExtentBackend):

        def text_extents(self, style, text):
            """
            The text extents are calculated using tkinter.Font
            """
            with InternalWindow() as window:
                font_type = ""
                if style["bold"]:
                    font_type += "bold"
                if style["italic"]:
                    if len(font_type) > 0:
                        font_type += " "
                    font_type += "italic"

                # Create the new font object.
                font = Font(window, (style["font"], -int(style["size"]*FONT_SCALING), font_type))
                # Query the data
                width = font.measure(text)
                metrics = font.metrics()

            return {
                "width": width / float(FONT_SCALING),
                "height": metrics["linespace"] / float(FONT_SCALING),
                "ascent": metrics["ascent"] / float(FONT_SCALING),
                "descent": metrics["descent"] / float(FONT_SCALING)
            }