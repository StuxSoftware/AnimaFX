#
# The MIT License (MIT)
#
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
Utility functions for kara-builder.
"""
__author__ = 'StuxCrystal'


class Time(int):
    """
    Represents a time index in ass.
    """

    def __new__(cls, time):
        if isinstance(time, basestring):
            time = from_ass_time(time)
        return int.__new__(cls, time)

    def __repr__(self):
        return "<Time %s>"%to_ass_time(self)

    def __str__(self):
        return to_ass_time(self)


def to_ass_time(time):
    """
    Converts the integer-time to a ass-time-value.
    """
    return "%d:%02d:%02d.%02d" % (
          time // (60*60*1000),
          time % (60*60*1000) // (60*1000),
          time % (60*1000) // 1000,
          time % 1000 // 10
    )


def from_ass_time(time):
    """
    Converts the ass-time to the time in milliseconds
    """
    h, m, _s = time.split(":")

    h, m = int(h), int(m)

    if "." in _s:
        # Split times.
        s, d = _s.split(".")

        s, d = int(s), int(d)

        # If time is given in deciseconds.
        if len(_s.split(".")[1]) == 2:
            d *= 10

    else:
        # If the milliseconds are not given.
        s = int(_s)
        d = 0

    # Calculate the times.
    return h*(60*60*1000) + m*(60*1000) + s*1000 + d


class cache(object):
    """
    Caches the function.
    """

    def __init__(self, func):
        self.called = False
        self.result = None
        self.func = func

        self.__doc__ = self.func.__doc__

    def __call__(self, *args, **kwargs):
        if self.called:
            return self.result
        self.called = True
        self.result = self.func(*args, **kwargs)
        return self.result