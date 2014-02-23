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
import math

from pykaraoke.calc import Vector
from pykaraoke.processors import ProcessingContext

from values import Value, DynamicValue, Type
from dumper import get_active_type, dump_tags

__author__ = 'StuxCrystal'


class Builder(object):
    """
    Represents a single builder.
    """

    def __init__(self, parent=None):
        self.context = ProcessingContext()
        self.children = []
        self.parent = parent

        if parent is not None:
            self.parent.children.append(self)

        self._initialize()

    def _initialize(self):
        self.context.tags = []
        # self.context.position = DynamicValue(lambda line: Vector(line["x"], line["y"]))

    def _get_lines(self):
        raise NotImplementedError("Subclasses have to implement the builder.")

    def tag(self, tag, calculator):
        """
        Adds a tag value. Do not use this value if other functions provide the same
        function.

        Examples:
        Adds \alpha as a frame4frame effect.
        >>> Builder.tag("alpha", lambda line, t: int(255*t))

        Adds \1c according to a list and the line index
        >>> Builder.tag("1c", lambda line: ["&H000000&", "&H00FF00", "&HFF0000"][line["index"]%3])

        Adds the tag as a static value
        >>> Builder.tag("fad", (0, 300))

        You can use the tag method multiple times
        >>> Builder.tag("fad", (0, 300)).tag("org", (400, 300))
        '{\\fad(0,300)\\org(400,300)}Text'

        Note:
        Vectors can be passed if the tag has the form "\\tag(x,y)".
        """
        self.context.tags.append((tag, Value.wrap(calculator)))
        return self

    def get_parent(self):
        return self.parent

    def to_document(self):
        raise NotImplementedError

    def _gather_tags(self, line, type):
        tags_line = []
        for tag_name, tag_value in self.context.tags:
            tags_line.extend(tag_value(line, type, tag_name))
        return tags_line


class DocumentBuilder(Builder):
    """
    A builder that uses the document to process objects.
    """

    def __init__(self, document, cls):
        Builder.__init__(self, None)
        self.document = document
        self.cls = cls

        self.fps = None
        self.prefer_video = True

        self.syllable_builder = None

    def _initialize(self):
        Builder._initialize(self)
        self.context.retime = None

    def _get_lines(self):
        return self.document.get_lines()

    def retime(self, func):
        self.context.retime = func
        return self

    def syllables(self):
        """
        Returns the syllables
        """
        if self.syllable_builder is None:
            self.syllable_builder = SyllableBuilder(self)
        return self.syllable_builder

    def to_document(self):
        """
        Generates the desired effect.
        """
        value_type = get_active_type(self)
        if value_type == Type.simple:
            result = self._create_simple()
        elif value_type == Type.frame4frame:
            result = self._create_frame4frame()
        else:
            raise ValueError("Unknown type.")
        pre_result = self.cls(result)
        return pre_result

    def _create_simple(self):
        """
        Generates the desired effect.
        """
        result = []
        for line in self.document:
            new_line = line.copy()
            new_line.text = None

            tags_line = self._gather_tags(line, Type.simple)

            if self.syllable_builder is not None:
                tags_syllable = []
                for sylline in self.cls([line]).syllables():
                    tags_syllable.append(self.syllable_builder._gather_tags(sylline, Type.simple))

                for index, syllable in enumerate(line.syllables):
                    actual_tags = [("r", "")] + tags_line + tags_syllable[index]
                    new_line = "".join(
                        (dump_tags(actual_tags) + syllable.text)
                    )
            else:
                new_line.text = dump_tags(tags_line) + line.text

            if self.context.retime is not None:
                new_line = self.cls([new_line]).retime(self.context.retime)[0]

            result.append(new_line)
        return result

    def _create_frame4frame(self):
        result = []

        document = self.document

        for line in self.document.frames(self.fps, self.prefer_video):
            new_line = line.copy()
            new_line.text = None

            text = line.text

            tags_line = self._gather_tags(line, Type.frame4frame)

            if self.syllable_builder is not None:
                text = line["syllable"].text
                sylline = line.copy()
                sylline["line_index"] = sylline["index"]
                sylline["line_count"] = sylline["count"]
                sylline["index"] = line["syl_index"]
                sylline["count"] = line["syl_count"]
                tags_line += self.syllable_builder._gather_tags(sylline, Type.frame4frame)

            new_line.text = dump_tags(tags_line) + text

            if self.context.retime is not None:
                new_line = self.cls([new_line]).retime(self.context.retime)[0]

            result.append(new_line)

        return result


class SyllableBuilder(Builder):

    def __init__(self, parent):
        Builder.__init__(self, parent)

    def to_document(self):
        return self.parent.to_document()