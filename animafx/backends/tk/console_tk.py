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
from tkinter import Tk

from animafx.singleton import singleton

__author__ = 'StuxCrystal'


class InternalWindow(singleton()):
    """
    Creates an internal window object that cannot be seen by the user.
    Note that this implementation may be very slow.
    """

    def __init__(self):
        self.window = None
        self.window_counter = 0

    def __enter__(self):
        if self.window is None:
            self.window = Tk()
            self.window.withdraw()

        return self.window

    def __exit__(self, exc_type, exc_val, exc_tb):
        return False

    def __del__(self):
        if self.window is not None:
            self.window.destroy()