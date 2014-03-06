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
from threading import Lock
__author__ = 'StuxCrystal'


class Singleton(type):
    """
    Metaclass for singletons.
    This metaclass creates thread-safe singletons.
    """

    # Lock to synchronize singleton instantiation.
    _lock = Lock()

    # Instance-Dictionary.
    _instances = {}

    def __call__(cls, *args, **kwargs):
        """
        Prevent metaclasses from being instantiated if they
        already were instantiated before.
        """
        with Singleton._lock:
            if cls not in Singleton._instances:
                Singleton._instances[cls] = super(Singleton, cls).__call__(*args, **kwargs)
            return Singleton._instances[cls]


def singleton(base=object):
    class Base(base, metaclass=Singleton):
        pass
    return Base