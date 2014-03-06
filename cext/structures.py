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
from collections import OrderedDict
import inspect
import ctypes

from cext.utils import six
from cext.utils.helpers import CData

__all__ = ["Structure", "Union"]


def _new_(ocls, cls, what, bases, attrs):
    """
    Generates the fields for __new__.

    Since Structures and Unions share the same code-base the code for __new__ is written
    into an external function.
    """
    if what == "NewBase" and attrs == {}:
        return super(ocls, cls).__new__(cls, what, bases, attrs)

    # Since Python3 can use ordered dicts as dict object, _order_ is only evaluated in Python2.
    auto_order = True
    order = attrs.keys()
    if "_order_" in attrs:
        if six.PY2:
            order = attrs["_order_"]
            if isinstance(order, six.string_types):
                order = order.replace(",", " ").split(" ")
            auto_order = False
        del attrs["_order_"]

    # If the class has already a fields attribute. Use the attribute instead of the given attribute.
    if "_fields_" in attrs:
        _fields_ = attrs['_fields_']
    else:
        # Generate the field list for this class.
        _fields_ = []

        for item in order:
            # Fail if we encounter any unknown items.
            if item not in attrs:
                raise AttributeError("Unknown attribute: " + item)

            attr = attrs[item]

            # Check if the attribute is a _CData attribute
            if inspect.isclass(attr) and issubclass(attr, CData):
                _fields_.append((item, attr))

                # Remove the item from the list since _Structure will add them for us.
                del attrs[item]
                continue

            # If _order_ was specified, throw an exception.
            elif not auto_order:
                raise AttributeError("The attribute '%s' is not a valid ctype." % item)

    attrs['_fields_'] = _fields_

    return super(ocls, cls).__new__(cls, what, bases, attrs)


# Structures #########################################################################################################


class StructureMeta(type(ctypes.Structure)):
    """
    The base class for structures.
    """

    def __prepare__(cls, bases):
        """
        Since Structures are ordered, use an ordered dict instance to enumerate them correctly.
        """
        return OrderedDict()

    def __new__(cls, what, bases, attrs):
        """
        Automatically create the new meta-field.
        """
        return _new_(StructureMeta, cls, what, bases, attrs)


class Structure(six.with_metaclass(StructureMeta, ctypes.Structure)):
    """
    Base-Class for structures.
    """
    pass


# Unions #############################################################################################################

class UnionMeta(type(ctypes.Union)):
    """
    The base class for unions.
    """

    def __prepare__(cls, bases):
        """
        Since Unions are ordered, use an ordered dict
        """
        return OrderedDict()

    def __new__(cls, what, bases, attrs):
        """
        Automatically create the new meta-field.
        """
        return _new_(UnionMeta, cls, what, bases, attrs)


class Union(six.with_metaclass(UnionMeta, ctypes.Union)):
    """
    Base-Class for unions.
    """
    pass