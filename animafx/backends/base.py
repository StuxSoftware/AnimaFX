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
Base-Classes for all backends.
"""
__author__ = 'StuxCrystal'


class TextExtentBackend(object):
    """
    Defines the base function for text-extents.
    """

    def text_extents(self, style, text):
        """
        Calculates the text-extents.
        style:{font:str, size:int, bold:bool, italic:bool, name:str} text:str
        --> {width:float, height:float, [ascent:float,] [descent:float,] [extlead:float,] [intlead:float,]}
        Note that height is optional if ascent and descent are given.
        """
        raise NotImplementedError


class InputDocumentBackend(object):
    """
    Defines the backend-functions for text-resolvers.
    """

    def get_syllables(self):
        """
        Returns all syllables of the document.
        --> [
              [
                # first item
                {
                  start:int, end:int,
                  style:{font:str, bold:bool, italic:bool, name:str, size:int, [, spacing:int]},
                  anchor:int,
                  margin: (l:int, r:int, v:int),
                  layer:int
                },

                # for each syllable
                {start:int (relative to start), duration:int, text:str}
              ]
            ]
        """
        raise NotImplementedError

    def get_viewport_size(self):
        """
        Returns the viewport size.
        ---> (width:int, height:int)
        """
        raise NotImplementedError

    def load(self, file):
        """
        Load the file.
        file:fd
        """
        raise NotImplementedError


class OutputDocumentBackend(object):
    """
    Defines the backend for the output document
    """

    def write_line(self, line):
        """
        Wrties a line into the document.
        {style:str, text:str, start:int, end:int, layer:int}
        """
        raise NotImplementedError

    def dump(self, file):
        """
        Dump the output
        file:fd
        """
        raise NotImplementedError