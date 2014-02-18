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
        tuple.__new__(cls, (x, y, z))

    # The components of the vector.
    x, y, z = [property(operator.itemgetter(i)) for i in range(3)]

    def set_length(self, length):
        """
        Sets the length of the vector.
        """
        return self/abs(self)*length

    def distance(self, other):
        """
        Returns the distance between the two vectors.
        """
        return abs(other - self)

    def __abs__(self):
        """
        Returns the length of the vector
        """
        return math.sqrt(self.x**2 + self.y**2 + self.z**2)

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

    def __mul__(self, other):
        """
        Multiplies a vector with a scalar.
        """
        return Vector(self.x*other, self.y*other, self.z*other)

    def __div__(self, other):
        """
        Divides a vector with another vector.
        """
        return Vector(self.x/other, self.y/other, self.z/other)

    def __repr__(self):
        return "<Vector x:%f y:%f z:%f>"%self