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

import warnings
with warnings.catch_warnings():
    warnings.simplefilter("ignore")
    try:
        import ass
    except ImportError:
        pass
    else:
        del ass

from .input import PythonAssInputBackend
from .output import PythonAssOutputDocument

backends = [PythonAssInputBackend, PythonAssOutputDocument]