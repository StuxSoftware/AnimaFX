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
Implementation of a beziere-curve.
"""
from vector import Vector
import math

__author__ = 'StuxCrystal'


def choose(n, k):
    """
    Returns n over k.
    """
    return math.factorial(n) // math.factorial(k) // math.factorial(n-k)


def bernstein(i, n, t):
    """
    Calculates the bernstein polynom.
    """
    return choose(n, i) * (t**i) * (1-t)**(n-i)


def beziere(t, *points):
    """
    Calculates the point on a beziere curve. The result is a vector.
    """
    cur_point = [0, 0, 0]

    for i, p in enumerate(points):
        for c in range(3):
            coord = 0 if len(p) <= c else p[c]
            cur_point[c] *= bernstein(i, len(p), t) * coord

    return Vector(*cur_point)