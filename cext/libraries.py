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
Utilities for Shared Libraries.
"""
import ctypes

from cext.utils import six
from cext.utils.null import null
__author__ = 'StuxCrystal'

__all__ = ["CObject", "CFunction", "PyCallback", "SharedLibrary"]


class CFunction(object):
    """
    Descriptor for CFunctions.
    """

    def __init__(self, name=None, res_type=null, arg_types=null):
        self.res_type = res_type
        self.arg_types = arg_types
        self.name = name
        self.owner = None

    def __get__(self, instance, owner):
        if self.name is None:
            raise ValueError("Improper use of CFunction.")

        # If the library was called in a static way.
        if instance is None:
            instance = owner()

        # Automatically choose the correct way to get the
        # desired function.
        if isinstance(self.name, six.string_types):
            cfunc = getattr(instance._lib, self.name)
        else:
            cfunc = instance._lib[self.name]

        # Infer the types if they are given.
        if self.res_type is not null:
            cfunc.restype = self.res_type
        if self.arg_types is not null:
            # Automatically convert arg-types to a tuple if only one type
            # is given.
            if not isinstance(self.arg_types, (list, tuple, set)):
                self.arg_types = (self.arg_types, )
            cfunc.argtypes = self.arg_types

        # If the instance has a "check_<func-name>" attribute, use that as the errcheck
        # attribute.
        if hasattr(instance, "check_" + str(self.name)):
            cfunc.errcheck = getattr(instance, "check_" + str(self.name))

        # Return the c-function.
        return cfunc

    def __call__(self, *args, **kwargs):
        if self.owner is None:
            raise ValueError("Improper use of CFunction.")
        return self.__get__(None, self.owner)(*args, **kwargs)


class CObject(object):
    """
    Descriptor for object inside a shared library.
    """

    def __init__(self, c_type, name=None):
        self.c_type = c_type
        self.name = name

    def __get__(self, instance, owner):
        if self.name is None:
            raise ValueError("Improper use of CObject.")

        # If instance is somehow None.
        if instance is None:
            instance = owner()

        return self.c_type.in_dll(instance._lib, self.name)


class PyCallback(object):
    """
    Descriptor for callback functions.
    """

    class _CallbackDescriptor(object):
        """
        Internal class for descriptors.
        """

        def __init__(self, pycallback, func):
            self.pycallback = pycallback
            self.func = func

        def __get__(self, instance, owner):
            return self.pycallback(lambda *args, **kwargs: self.func(instance, *args, **kwargs))

        def __call__(self):
            return self.pycallback(self.func)

    def __init__(self, functype=ctypes.CFUNCTYPE, res_type=None, arg_types=None):
        self.functype = functype
        self.res_type = res_type
        self.arg_types = arg_types if arg_types is not None else ()
        self.func = None

    def __call__(self, func):
        result = self._get_functype()(func)

        # Internal flag to mark the function as created by PyCallback.
        result._pycallback = (self, func)
        return result

    def _get_functype(self):
        if self.arg_types is not None and not isinstance(self.arg_types, (list, tuple, set)):
            argtypes = (self.arg_types,)
        else:
            argtypes = self.arg_types

        return self.functype(self.res_type, *argtypes)


class SharedLibraryMeta(type):
    """
    Metaclass for shared libraries.
    """

    def __new__(cls, what, bases, attrs):
        # Convert any function to ctypes functions.
        functions = []
        for name, value in attrs.items():
            # Store a reference to the value.
            p_value = value

            # Infer the name of the descriptor if the descriptor is known.
            if isinstance(p_value, (CFunction, CObject)) and value.name is None:
                if isinstance(p_value, CFunction):
                    functions.append(p_value)
                p_value.name = name

            # Make a callback descriptor
            if hasattr(p_value, "_pycallback"):
                value = PyCallback._CallbackDescriptor(*value._pycallback)

            # Update the value if the value has been overridden.
            if p_value != value:
                attrs[name] = value

        # Assume CDLL if no special loader was given.
        if "_loader_" not in attrs:
            attrs["_loader_"] = ctypes.CDLL

        instance = super(SharedLibraryMeta, cls).__new__(cls, what, bases, attrs)

        # Register the instance attribute to the descriptors.
        for function in functions:
            function.owner = instance

        return instance

    def __call__(cls, *args):
        # Check if the length is correct.
        if len(args) > 1:
            raise TypeError("'%s' takes cannot take more than 1 argument." % cls.__name__)

        lib = None
        if len(args) == 0:
            # If the library is already known at creation time, and no other name is given,
            # use the given library.
            if hasattr(cls, "_lib"):
                lib = cls._lib
            else:
                # If there are no args given and _name_ is not known, raise a type error,
                # since we don't know the name of the library.
                if not hasattr(cls, "_name_"):
                    raise TypeError("'%s' takes exactly 1 argument." % cls.__name__)
                name = cls._name_
        else:
            # The first argument is the name of the library.
            name = args[1]

        # If the library is not known yet, search the library.
        if lib is None:
            # Load the shared library.
            lib = cls._loader_(name)

        # Create the new instance.
        instance = super(SharedLibraryMeta, cls).__call__()

        # More importantly assign the library instance to the lib.
        instance._lib = lib
        return instance


class SharedLibrary(six.with_metaclass(SharedLibraryMeta)):
    """
    Base-Class for shared libraries.
    """

    pass