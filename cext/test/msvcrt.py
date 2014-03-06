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
__author__ = 'StuxCrystal'

from cext import SharedLibrary, PyCallback, CFunction
import ctypes
import platform

if platform.platform(True).startswith("Windows"):
    libc_name = "msvcrt"
else:
    libc_name = "c"


class LibC(SharedLibrary):
    _loader_ = ctypes.CDLL
    _name_ = libc_name

    _qsort = CFunction("qsort", res_type=None)

    if libc_name == "msvcrt":
        strlen = CFunction("wcslen", arg_types=ctypes.c_wchar_p)
    else:
        strlen = CFunction()

    comparator = PyCallback(res_type=ctypes.c_int, arg_types=(ctypes.POINTER(ctypes.c_int), ctypes.POINTER(ctypes.c_int)))

    def qsort(self, func, *args):
        ints = (ctypes.c_int * len(args))(*args)
        self._qsort(ints, len(ints), ctypes.sizeof(ctypes.c_int), func)
        return ints

if __name__ == "__main__":
    @LibC.comparator
    def cmp(a, b):
        return b[0]-a[0]
    x = LibC()

    print(*x.qsort(cmp, 5,78,2,45,48))
    print(x.strlen("Oh Yeah"))
    print(x.strlen(ctypes.create_unicode_buffer("Oh Yeah")))

    print(LibC.strlen("test"))
    print(LibC._qsort)
    print(cmp)