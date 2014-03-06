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
__author__ = 'StuxCrystal'

__all__ = ["backends"]

import platform

#
# Do not import anything if the current platform is not windows.
#
if not platform.platform(aliased=True).startswith("Windows"):
    backends = []

else:
    from animafx.backends.windows.text_extents import GDI32Backend
    backends = [GDI32Backend]