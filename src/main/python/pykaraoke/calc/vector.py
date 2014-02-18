"""
Implementation of vectors.
"""
__author__ = 'Stux'

import operator
import math

__all__ = ["Vector"]


class Vector(tuple):
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
        """
        return Vector(self.x*other, self.y*other, self.z*other)

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

    # Representation
    def __repr__(self):
        return "<Vector x:%f y:%f z:%f>"%self