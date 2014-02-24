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

import numbers
import operator
import math

__all__ = ["Vector"]


class Vector(tuple):
    """
    This class represents a two-dimensional vector

    >>> Vector(1, 2)
    <Vector x:1.000000  y:2.000000>

    You can add, substract two vectors
    >>> Vector(1,2) + Vector(2,3)
    <Vector x:3.000000 y:5.000000>
    >>> Vector(5,5) - Vector(8,9)

    You can multiply and divide a vector with a scalar.
    >>> Vector(4,5) * 7
    >>> Vector(4,5) / 8

    You can calculate the dot-product by multiplying multiple vectors
    >>> Vector(1,2) * Vector(4,5)

    You can negate (or make positive) and a vector
    >>> -Vector(4,5)
    >>> +Vector(4,5)

    You can calculate the distance relative to the origin
    >>> abs(Vector(4,5))

    You can check if the vector is a null-vector or check if two vectors equals each other
    >>> True if Vector(0,0,0) else False
    >>> Vector(1,2) == Vector(4,5)
    >>> Vector(7,8) != Vector(7,5)

    You can query and unpack each of its components.
    >>> Vector(1,2)[0] == Vector(1,2).x
    >>> Vector(1,2)[1] == Vector(1,2).y
    >>> Vector(1,2)[2] == Vector(1,2).z
    >>> x, y, z = Vector(1,3)

    You can normalize the vector
    >>> Vector(5,2).normalize()


    Note that this class is immutable.
    """

    # This is an immutable object, thus there are no
    # objects.
    __slots__ = []

    def __new__(cls, x, y):
        """
        Use tuples __new__ method to make sure the values are immutable.

        Note that z is 1. Changing this coordinate causes AffineTransform
        to return invalid translation values.
        """
        return tuple.__new__(cls, (float(x), float(y)))

    # The components of the vector.
    x, y = [property(operator.itemgetter(i)) for i in range(2)]

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
        return Vector(self.x+other.x, self.y+other.y)

    def __sub__(self, other):
        """
        Substracts two vectors.
        """
        return Vector(self.x-other.x, self.y-other.y)

    def __neg__(self):
        """
        Negates a vector.
        """
        return Vector(-self.x, -self.y)
    __invert__ = __neg__

    def __pos__(self):
        """
        Makes a vector positive
        """
        return Vector(+self.x, +self.y)

    def __mul__(self, other):
        """
        Multiplies a vector with a scalar.
        Will calculate the dot-product if you pass another vector to it.
        """
        if isinstance(other, Vector):
            return self._m_dot(other)
        return self._m_scalar(other)

    def _m_scalar(self, other):
        return Vector(self.x*other, self.y*other)

    def _m_dot(self, other):
        return self.x*other.x + self.y*other.y

    def __div__(self, other):
        """
        Divides a vector with another vector.
        """
        return Vector(self.x/float(other), self.y/float(other))

    # Comparison
    def __nonzero__(self):
        """
        Checks if this is not a null vector.
        """
        return self.x != 0 and self.y != 0

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
        return self.x**2 + self.y**2

    def get_angle(self, vector):
        """
        Returns the angle between two vectors (in radians).
        """
        return math.acos((self*vector) / abs(self) / abs(vector))

    # Representation
    def __repr__(self):
        return "<Vector x:%f y:%f z:%f>" % self


class AffineTransform(tuple):
    """
    This class represents an affine transformation.
    """

    def __new__(cls, rows=(((0,)*3,)*3), row2=None, row3=None):
        if row2 is not None and row3 is not None:
            rows = (rows, row2, row3)
        elif row2 is not None or row3 is not None:
            raise ValueError("One or three arguments have to be passed to this class.")

        if len(rows) != 3:
            raise ValueError("Invalid transform matrix")
        for row in rows:
            if len(row) != 3:
                raise ValueError("Invalid transform matrix")

        return tuple.__new__(cls, tuple((tuple(row) for row in rows)))

    def _operation(self, operation, other, is_matrix):
        return AffineTransform(
            tuple((
                tuple((operation(self[x][y], (other[x][y] if is_matrix else other)) for y in range(3)))
                for x in range(3)
            ))
        )

    def __add__(self, other):
        return self._operation(operator.add, other, True)

    def __sub__(self, other):
        return self._operation(operator.sub, other, True)

    def __mul__(self, other):
        if isinstance(other, numbers.Number):
            return self._operation(operator.mul, other, False)
        elif isinstance(other, Vector):
            data = (other.x, other.y, 1)
            return Vector(*[sum([data[x] * self[x][y] for y in range(3)]) for x in range(3)][:2])
        elif isinstance(other, AffineTransform):
            return AffineTransform(
                tuple((
                    tuple((
                        sum([self[x][z] * other[z][y] for z in range(3)])
                    ) for y in range(3))
                ) for x in range(3))
            )
        raise TypeError("Unsupported operand types '%s' and '%s'" % (self.__class__.__name__, other.__class__.__name__))

    def __div__(self, other):
        return self._operation(operator.truediv, other, False)

    def __repr__(self):
        return "<AffineTransform %r %r %r>" % self

    @staticmethod
    def identity():
        """
        Returns the identity matrix for the vectors.
        """
        return AffineTransform(
            (1.0, 0.0, 0.0),
            (0.0, 1.0, 0.0),
            (0.0, 0.0, 1.0)
        )

    @staticmethod
    def scale(x=1.0, y=1.0):
        """
        Returns an affine transformation for scaling.
        """
        return AffineTransform(
            (x,   0.0, 0.0),
            (0.0, y,   0.0),
            (0.0, 0.0, 1.0)
        )

    @staticmethod
    def flip(x=False, y=False):
        """
        Flips the object around the given axis.
        """
        return AffineTransform.scale(*(1.0 * (-bool(c)) for c in (x, y, False)))

    @staticmethod
    def move(x=0.0, y=0.0):
        """
        Moves the object.
        """
        return AffineTransform(
            (1.0, 0.0, float(x)),
            (0.0, 1.0, float(y)),
            (0.0, 0.0, 1.0)
        )

    @staticmethod
    def rotate(rx=0, ry=0, rz=0, p=Vector(0, 0, 1)):
        tt = AffineTransform.move(p[0], p[1])
        tf = AffineTransform.move(-p[0], -p[1])

        tx = AffineTransform(
            (1.0, 0.0,          0.0),
            (0.0, math.cos(rx), -math.sin(rx)),
            (0.0, math.sin(rx), math.cos(rx))
        )

        ty = AffineTransform(
            (math.cos(ry),  0.0, math.sin(ry)),
            (0.0,           1.0, 0.0),
            (-math.sin(ry), 0.0, math.cos(ry))
        )

        tz = AffineTransform(
            (math.cos(rz), -math.sin(rz), 0.0),
            (math.sin(rz), math.cos(rz),  0.0),
            (0.0,          0.0,           1.0)
        )

        return tt*tx*ty*tz*tf

    @staticmethod
    def shear(sx=1.0, sy=1.0):
        return AffineTransform(
            (1.0, sx,  0.0),
            (sy,  1.0, 0.0),
            (0.0, 0.0, 1.0)
        )