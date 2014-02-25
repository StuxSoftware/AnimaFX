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

from pykaraoke.environment import Environment, set_environment

from .backends import get_backend
from .backends.base import TextExtentBackend
from .singleton import Singleton

from .version import __version__ as _version

__all__ = ["AnimaFXEnvironment"]

class AnimaFXEnvironment(Environment):
    """
    Implementation of the anima-fx environment
    """

    # This class is actually a singleton so
    # prevent it from be reinstantiated.
    __metaclass__ = Singleton

    def __init__(self, animafx):
        """
        Registers the backend-values.
        """
        super(AnimaFXEnvironment, self).__init__((), ())
        self.animafx = animafx
        self.text_extent_backend = None

    def _text_extents(self, style, text):
        # Resolve the backend if neccessary.
        if self.text_extent_backend is None:
            self.text_extent_backend = get_backend(TextExtentBackend)

            if self.text_extent_backend is None:
                raise NotImplementedError("No backend was found to calculate the extents.")

        return self.text_extent_backend.text_extents(style, text)

    def log(self, level, msg, t, logger):

        pass

    def __repr__(self):
        return "<Environment of AnimaFX %s>" % _version

    @classmethod
    def register(cls):
        """
        Registers the environment and returns the newly created environment.
        """
        environment = cls()
        set_environment(environment)