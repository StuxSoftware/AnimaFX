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

from . import tk, pythonass

__all__ = []

# List of all backends
_BACKENDS = [tk.backends, pythonass.backends]


def register_backend(backend):
    """
    Registers a new backend-provider.
    """
    _BACKENDS.insert(0, backend)


def get_backend(cls):
    """
    Returns the first working backend-provider (iterable)
    """
    for backend in _BACKENDS:
        for impl in backend:
            if impl is None:
                continue

            if issubclass(impl, cls):
                return impl()
