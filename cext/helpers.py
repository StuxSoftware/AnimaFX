#
# The MIT License (MIT)
#
# CExt - ctypes extension framework
# Copyright (c) 2014 StuxCrystal
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all
# copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
# SOFTWARE.
#
"""
Helper functions for CExt usage.
"""
import ctypes
import inspect

from cext.utils.helpers import CData


def pointer(obj, type):
    """
    Pointer(cls[, 1]) -> ctypes.POINTER(obj)
    Pointer(cls, depth) -> ctypes.POINTER(ctypes.POINTER(...(obj)))
    Pointer(obj) -> ctypes.pointer(obj)
    Pointer(obj, type) -> ctypes.pointer(type(obj))

    Makes a pointer. If you pass a type object, you get a Pointer-Type.
    If the object has an attribute "_as_parameter_" (either as a property or as a plain attribute),
    the value in _as_parameter_ will be used.

    If type is given, the object is wrapped into type first before its pointer will be returned.

    If a type is an integer and obj is a ctype-class, the type will be a pointer to the given degree
    of
    """
    if inspect.isclass(obj) and issubclass(obj, CData):
        if type is None:
            type = 1
        for i in range(type):
            obj = ctypes.POINTER(obj)
        return obj

    if hasattr(obj, "_as_parameter_"):
        obj = obj._as_parameter_

    if type is not None:
        obj = type(obj)
    return ctypes.pointer(obj)