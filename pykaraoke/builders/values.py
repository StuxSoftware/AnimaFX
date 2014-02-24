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
import inspect

from ..utils import create_enum

__author__ = 'StuxCrystal'

__all__ = ["Type", "Value", "StaticValue", "DynamicValue"]

Type = create_enum("Type", "simple", "frame4frame")


def acceleration(value):
    """
    This decorator sets the acceleration for the
    function that is decorated with this decorator.

    Only for Frame4Frame-Functions.
    """
    def _decorator(func):
        func.accel = value
    return _decorator


class Value(object):
    """
    Represents a simple value type.
    """

    def get_type(self):
        raise NotImplementedError

    def __call__(self, line, type, tag):
        raise NotImplementedError

    @staticmethod
    def wrap(func):
        """
        Wraps the function into a value object.
        """
        # Do not do anything if the passed object is
        # a value object.
        if isinstance(func, Value):
            return func

        # If the passed object is not callable, wrap
        # it into a static value
        if not callable(func):
            return StaticValue(func)

        args, varargs, keywords, default = inspect.getargspec(func)

        # Assume that the given value is a frame4frame
        # function if the function has two arguments.
        if len(args) == 2:
            # If the function has an meber called accel,
            # use this as the acceleration value for this
            # FunctionValue.
            accel = 1.0
            if hasattr(func, "accel"):
                accel = func.accel

            return FunctionValue(func, accel)

        # Assume that the given function is a dynamic value
        # if the function has only one argument.
        elif len(args) == 1:
            return DynamicValue(func)

        # Assume that the function will return static values
        # if the function has only one argument.
        elif len(args) == 0:
            return StaticValue(func)


class StaticValue(Value):
    """
    Represents a static value.
    """

    def __init__(self, value):
        self.value = value

    def get_type(self):
        return Type.simple

    def __call__(self, line, type, tag):
        value = self.value
        if callable(self.value):
            value = self.value()

        return (tag, value),


class DynamicValue(Value):
    """
    Represents a value that depends on the passed line.

    The passed function must have a single argument line.
    """

    def __init__(self, func):
        self.func = func

    def get_type(self):
        return Type.simple

    def __call__(self, line, type, tag):
        if type == Type.frame4frame:
            line = line["original"]
        return (tag, self.func(line)),


class FunctionValue(Value):
    """
    Represents a value that uses frame for frame.

    The passed function must have the arguments
    func(line, t)
    | line:Line  - The line object to use
    | t   :float - The current time 0<=t<=1
    """
    def __init__(self, func, accel=1.0):
        self.accel = accel
        self.func = func

    def get_type(self):
        return Type.frame4frame

    def __call__(self, line, type, tag):
        return ((tag, self.func(line, (float(line["index"])/line["count"]) ** self.accel))),