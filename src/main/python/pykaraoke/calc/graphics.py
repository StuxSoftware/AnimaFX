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
Implementation of vectors.
"""
__author__ = 'StuxCrystal'

import operator
import math

__all__ = ["Vector"]


class Vector(tuple):
    """
    This class represents a three-dimensional vector.

    >>> Vector(1, 2, 3)
    <Vector x:1.000000  y:2.000000 z:3.000000>
    >>> Vector(1, 2)                              # The z-Argument is optional.
    <Vector x:1.000000  y:2.000000 z:0.000000>

    You can add, substract two vectors
    >>> Vector(1,2,3) + Vector(2,3,4)
    >>> Vector(5,5,6) - Vector(8,9,5)

    You can multiply and divide a vector with a scalar.
    >>> Vector(4,5,6) * 7
    >>> Vector(4,5,6) / 8

    You can calculate the dot-product by multiplying multiple vectors
    >>> Vector(1,2,3) * Vector(4,5,6)

    You can negate (or make positive) and a vector
    >>> -Vector(4,5,9)
    >>> +Vector(4,5,6)

    You can calculate the distance relative to the origin
    >>> abs(Vector(4,5,6))

    You can check if the vector is a null-vector or check if two vectors equals each other
    >>> True if Vector(0,0,0) else False
    >>> Vector(1,2,3) == Vector(4,5,6)
    >>> Vector(7,8,9) != Vector(7,5,4)

    You can query and unpack each of its components.
    >>> Vector(1,2,3)[0] == Vector(1,2,3).x
    >>> Vector(1,2,3)[1] == Vector(1,2,3).y
    >>> Vector(1,2,3)[2] == Vector(1,2,3).z
    >>> x, y, z = Vector(1,2,3)

    You can normalize the vector
    >>> Vector(5,2,0).normalize()


    Note that this class is immutable.
    """

    # This is an immutable object, thus there are no
    # objects.
    __slots__ = []

    def __new__(cls, x, y, z=0):
        """
        Use tuples __new__ method to make sure the values are immutable.
        """
        return tuple.__new__(cls, (float(x), float(y), float(z)))

    # The components of the vector.
    x, y, z = [property(operator.itemgetter(i)) for i in range(3)]

    def set_length(self, length):
        """
        Sets the length of the vector.
        """
        return self/abs(self)*length

    def normalize(self):
        """
        Normalizes the vectors.
        """
        return self/abs(self)

    def distance(self, other):
        """
        Returns the distance between the two vectors.
        """
        return abs(other - self)

    def __abs__(self):
        """
        Returns the length of the vector
        """
        return math.sqrt(self.length_squared)

    def __add__(self, other):
        """
        Adds two vectors.
        """
        return Vector(self.x+other.x, self.y+other.y, self.z+other.z)

    def __sub__(self, other):
        """
        Substracts two vectors.
        """
        return Vector(self.x-other.x, self.y-other.y, self.z-other.z)

    def __neg__(self):
        """
        Negates a vector.
        """
        return Vector(-self.x, -self.y, -self.z)
    __invert__ = __neg__

    def __pos__(self):
        """
        Makes a vector positive
        """
        return Vector(+self.x, +self.y, +self.z)

    def __mul__(self, other):
        """
        Multiplies a vector with a scalar.
        Will calculate the dot-product if you pass another vector to it.
        """
        if isinstance(other, Vector):
            return self._m_dot(other)
        return self._m_scalar(other)

    def _m_scalar(self, other):
        return Vector(self.x*other, self.y*other, self.z*other)

    def _m_dot(self, other):
        return self.x*other.x + self.y*other.y + self.z*other.z

    def __div__(self, other):
        """
        Divides a vector with another vector.
        """
        return Vector(self.x/float(other), self.y/float(other), self.z/float(other))

    # Comparison
    def __nonzero__(self):
        """
        Checks if this is not a null vector.
        """
        return self.x != 0 and self.y != 0 and self.z != 0

    def __eq__(self, other):
        """
        Checks if the two vectors are equal.
        """
        for index, component in enumerate(self):
            if other[index] != component:
                return False
        return True
    __ne__ = lambda self, other: not __eq__(self, other)

    @property
    def length_squared(self):
        return self.x**2 + self.y**2 + self.z**2

    def get_angle(self, vector):
        """
        Returns the angle between two vectors (in radians).
        """
        return math.acos((self*vector) / abs(self) / abs(vector))

    # Representation
    def __repr__(self):
        return "<Vector x:%f y:%f z:%f>"%self