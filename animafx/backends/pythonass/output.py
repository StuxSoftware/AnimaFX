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
Writes ass-documents using python-ass <https://github.com/rfw/python-ass>
"""
__author__ = 'StuxCrystal'

try:
    import ass.document
except ImportError:
    PythonAssOutputDocument = None
else:
    from pykaraoke.core.utils import to_ass_time
    from ..base import OutputDocumentBackend

    class PythonAssOutputDocument(OutputDocumentBackend):
        """
        Output for AnimaFX.
        """
        def __init__(self):
            self.document = None

        def write_line(self, line):
            if self.document is None:
                raise RuntimeError("No document was loaded.")
            assert isinstance(self.document, ass.document.Document)
            self.document.events.append(ass.document.Dialogue(
                layer=line["layer"],
                start=to_ass_time(line["start"]),
                end=to_ass_time(line["end"]),
                style=line["style"],
                text=line["text"]
            ))

        def load(self, infile):
            self.document = ass.document.Document()
            input = ass.document.Document.parse_file(infile)
            self.document.styles = input.styles
            self.document.fields = input.fields

        def dump(self, outfile):
            self.document.dump_file(outfile)