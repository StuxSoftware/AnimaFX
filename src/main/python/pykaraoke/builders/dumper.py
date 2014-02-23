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
from pykaraoke.calc import Vector

from values import Type

__author__ = 'StuxCrystal'


def dump_tags(tags):
    """
    Dumps the tags.
    """
    result = ""
    for tag_name, value in tags:
        result += "\\" + tag_name
        if isinstance(value, Vector):
            value = Vector.x, Vector.y

        if isinstance(value, (list, tuple)):
            result += "(" + ",".join((str(item) for item in value)) + ")"
        else:
            result += str(value)
    return "{" + result + "}"


def get_active_type(builder):
    """
    Returns the active type of the builder.
    """
    current_type = Type.simple

    # Check all tags.
    for tag_name, tag_value in builder.context.tags:
        type = tag_value.get_type()
        if type > current_type:
            current_type = type

    # Check all childs.
    for child in builder.children:
        type = get_active_type(child)
        if type > current_type:
            type = current_type

    return current_type