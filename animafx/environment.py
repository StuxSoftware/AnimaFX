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
Represents the environment for AnimaFX.
"""
__author__ = 'StuxCrystal'
import io

from pykaraoke.environment import Environment, set_environment

from .backends import get_backend
from .backends.base import TextExtentBackend, InputDocumentBackend, OutputDocumentBackend
from .singleton import singleton

from .version import __version__ as _version

__all__ = ["AnimaFXEnvironment"]


class AnimaFXEnvironment(singleton(Environment)):
    """
    Implementation of the anima-fx environment
    """
    def __init__(self, animafx=None):
        """
        Registers the backend-values.
        """
        super(AnimaFXEnvironment, self).__init__((), ())
        self.animafx = animafx
        self.text_extent_backend = None
        self.infile_processor = None
        self.outfile_processor = None

        self._register()

    def feed(self, infile):
        """
        Prepares the infile and the outfile-processors.
        """
        buf = AnimaFXEnvironment._buf(infile)
        self.infile_processor = get_backend(InputDocumentBackend)
        self.outfile_processor = get_backend(OutputDocumentBackend)

        if self.infile_processor is None or self.outfile_processor is None:
            raise NotImplementedError("Input- or Output-Processors unknown.")

        # Feed the document processors.
        AnimaFXEnvironment._feed(self.infile_processor, buf)
        AnimaFXEnvironment._feed(self.outfile_processor, buf)

    def dump(self, outfile):
        """
        Dumps the actual file.
        """
        self.outfile_processor.dump(outfile)

    @staticmethod
    def _feed(processor, buf):
        buf_io = io.BytesIO(buf)
        with io.TextIOWrapper(buf_io) as stream:
            processor.load(stream)
        buf_io.close()

    @staticmethod
    def _buf(stream):
        buf = stream.read()
        stream.close()
        return buf

    def _text_extents(self, style, text):
        # Resolve the backend if neccessary.
        if self.text_extent_backend is None:
            self.text_extent_backend = get_backend(TextExtentBackend)

            if self.text_extent_backend is None:
                raise NotImplementedError("No backend was found to calculate the extents.")

        return self.text_extent_backend.text_extents(style, text)

    def get_syllables(self):
        return self.infile_processor.get_syllables()

    def get_viewport_size(self):
        return self.infile_processor.get_viewport_size()

    def write_line(self, line):
        self.outfile_processor.write_line({
            "style": line.style.name,
            "text": line.text,
            "start": line.start,
            "end": line.end,
            "layer": line.layer
        })

    @property
    def support(self):
        result = ["output"]

        return result

    @support.setter
    def support(self, value):
        pass

    def __repr__(self):
        return "<Environment of AnimaFX %s>" % _version

    def _register(self):
        """
        Registers the environment and returns the newly created environment.
        """
        # print("Registering: ", self)
        set_environment(self)