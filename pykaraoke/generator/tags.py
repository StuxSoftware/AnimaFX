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
Container for tag data.
"""
__author__ = 'StuxCrystal'


class Tag(object):
    """
    Represents a tag.
    """

    def __init__(self, tag, value):
        self.tag = tag
        self.value = value

    def generate_value(self):
        try:
            iterator = iter(self.value)
        except TypeError:
            return str(self.value)

        return "".join(["(", ",".join(
            (
                str(item)
                for item in iterator
            )
        ), ")"])

    def __str__(self):
        return "\\{tag}{value}".format(
            tag=self.tag, value=self.generate_value()
        )


def generate_line(tags, text=""):
    """
    generate_line(tags:str..., text:str="") -> str
    Generates a new line.
    """
    if len(tags) == 0:
        return ""

    return "".join(
        ["{"] + [tag.generate_value() for tag in tags] + ["}", text]
    )


class TagContainer(object):
    """
    Build multiple tags.
    """

    def __init__(self):
        self.tags = {}
        self.order = []
        self.och = False

    def tag(self, name, func):
        if not callable(func):
            _old = func
            func = lambda pre, line: _old
        self.tags.setdefault(name, []).append(func)
        if not self.och and name in self.order:
            self.order.append(name)

    def reorder(self, *tags):
        self.order = tags
        self.och = True

    def generate(self, line, text=""):
        tags = []
        for name in self.order:
            if name not in self.tags:
                continue
            val = None
            for func in self.tags[name]:
                nval = func(val, line)
                if nval is not None:
                    val = nval
            if val is not None:
                tags.append(Tag(name, val))

        return generate_line(tags, text)