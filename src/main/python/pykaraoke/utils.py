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