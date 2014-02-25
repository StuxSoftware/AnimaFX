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
Loads ass-documents using python-ass <https://github.com/rfw/python-ass>
"""
__author__ = 'StuxCrystal'

# Test if python-ass is available.
try:
    import ass.document

# If not, fail.
except ImportError:
    PythonAssInputBackend = None

# Use the actual implementation if so.
else:
    import re
    from ..base import InputDocumentBackend

    KARAOKE = re.compile(r"(?:^|\{\\k([0-9]+)})([^{]+)")

    class PythonAssInputBackend(InputDocumentBackend):
        """
        Loads the ass file when the
        """

        def __init__(self):
            """
            Initializes the backend.
            """
            self.document = None

        def load(self, file):
            """
            Load the document.
            """
            with file:
                self.document = ass.document.Document.parse_file(file)

        def get_viewport_size(self):
            """
            Returns the size of the viewport.
            """
            if self.document is None:
                raise RuntimeError("No document was loaded...")

            return self.document.play_res_x, self.document.play_res_y

        def get_syllables(self):
            """
            Returns the syllables if the document.
            """
            if self.document is None:
                raise RuntimeError("No document was loaded...")

            styles = {}
            for style in self.document.styles:
                data = {
                    "name": style.name,
                    "font": style.fontname,
                    "size": style.fontsize,
                    "bold": style.bold,
                    "italic": style.italic,
                    "spacing": style.spacing
                }
                styles[style.name] = (data, style.alignment)

            if "Default" not in styles:
                styles["Default"] = ({
                    "name": "Default",
                    "font": "Arial",
                    "size": 20,
                    "bold": False,
                    "italic": False,
                    "spacing": 0
                }, 2)

            result = []
            for line in self.document.events:
                line_info = []
                line_data = {
                    "start": line.start.seconds*1000 + line.start.microseconds//1000,
                    "end": line.end.seconds*1000 + line.end.microseconds//1000,
                    "margin": (line.margin_l, line.margin_r, line.margin_v),
                    "layer": line.layer
                }

                line_info.append(line_data)

                if line.style in styles:
                    line_data["style"] = styles[line.style][0]
                    line_data["anchor"] = styles[line.style][1]
                else:
                    line_data["style"] = styles["Default"][0]
                    line_data["anchor"] = styles["Default"][1]

                time = 0
                for dur, text in (match.groups() for match in KARAOKE.finditer(line.text)):
                    if len(dur) == 0:
                        dur = 0
                    else:
                        dur = int(dur)

                    line_info.append({
                        "start": time,
                        "duration": dur,
                        "text": text
                    })

                    time += dur*10

                result.append(line_info)
            return result