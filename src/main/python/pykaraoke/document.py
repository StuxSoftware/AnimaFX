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
Represents the input and the output document.
"""
import math

from builders import DocumentBuilder

from styles import StyleManager
from processors import process as process_lines
from environment import get_environment
from structures import Line, Viewport
from utils import from_ass_time, Time
__author__ = 'StuxCrystal'


__all__ = [
    "Document",
    "LineBuffer"
    "EnvironmentDocument", "InputDocument", "OutputDocument"
]


class Document(object):
    """
    Represents a simple document.

    Reading document objects are like builders:
    Non-Terminal Operations return itself.
    """

    def get_styles(self):
        """
        Returns the styles of the document.
        """
        raise NotImplementedError

    def get_style(self, name):
        """
        Returns the style of the document with the given name.
        """
        raise NotImplementedError

    def add_lines(self, lines):
        """
        Adds multiple lines.
        """
        for line in lines:
            self.add_line(line)

    def add_line(self, line):
        """
        Adds a line to the document.
        """
        raise NotImplementedError

    def get_lines(self):
        """
        Returns all lines of the document.
        """
        return [line.copy() for line in self._get_lines()]

    def _get_lines(self):
        """
        Returns all lines of the document.
        """
        raise NotImplementedError

    def process(self, *processors):
        """
        Processes all lines in the document with the given processors.
        """
        if not self.supports_line_reading:
            raise NotImplementedError

        process_lines(self._get_lines(), *processors)
        return self

    def annotate(self, func):
        """
        Executes the given function on all lines.
        """
        if not callable(func):
            raise ValueError("Cannot execute the function...")

        for line in self._get_lines():
            func(line)

        return self

    def set_text(self, func):
        """
        Sets the text of the lines.
        """
        def _set(line):
            line.text = func(line)
        return self.annotate(_set)

    def set_value(self, name, func):
        """
        Sets a value of the line. This can either be an extension
        or a actual value.
        """
        def _set(line):
            result = func(line)

            if hasattr(line, name):
                setattr(line, name, result)
            else:
                line[name] = result
        return self.annotate(_set)

    def retime(self, func):
        """
        Retimes the line. The result of the transformer is a tuple (absolute:bool, new_start:int, new_end:int)
        If new_start or new_end are None, they are ignored.
        """
        def _set(line):
            absolute, new_start, new_end = func(line)
            if absolute:
                new_start = line.start if new_start is None else new_start
                new_end = line.end if new_end is None else new_end
            else:
                new_start = (0 if new_start is None else new_start) + line.start
                new_end = (0 if new_end is None else new_end) + line.end

            line.start, line.end = new_start, new_end
        return self.annotate(_set)

    def refactor(self, func):
        """
        Refactors each line. e.g makes multiple lines.
        Will create a buffer that will be returned.

        The line passed to the function is a copy of the underlying line.
        """
        result = []

        for line in self.get_lines():
            refactor_result = func(line)
            if isinstance(refactor_result, Line):
                result.append(refactor_result)
            else:
                result.extend(refactor_result)

        return LineBuffer(result)

    def filter(self, func):
        """
        Filters a line.
        """
        def _refactor(line):
            return [line] if func(line) else []
        return self.refactor(_refactor)

    def frames(self, fps=None, prefer_video=False):
        """
        Creates a frame for frame effect.
        """
        environment = get_environment()
        if fps is None and not environment.video_info_supported:
            return lambda line: [line]

        if prefer_video and environment.video_info_supported:
            fps = environment.get_fps()

        duration = 1000.0/fps

        def _refactor(line):
            result = []
            cur_time = line.start
            index = 0

            while cur_time <= line.end:
                cur_line = line.copy()
                cur_line.extension = {}
                cur_line.start = max(line.start, int(math.floor(cur_time)))
                cur_time += duration
                cur_line.end = min(int(math.ceil(cur_time)), line.end)

                cur_line["original"] = line
                cur_line["index"] = index

                result.append(cur_line)

                index += 1

            line_count = len(result)
            for l in result:
                l["count"] = line_count

            return result
        return self.refactor(_refactor)

    def syllables(self):
        """
        Makes a syllable wise effect out of them.
        """
        return self.refactor(lambda line: [
            Line(
                syllable.start, syllable.end, line.style,
                margin=line.margin, anchor=line.anchor,
                extensions={"syllable": syllable, "line": line},
                text=syllable.text
            )
            for syllable in line.syllables
        ])

    def stream_to(self, document):
        """
        Streams the contents to the specified document.

        Returns the given document.
        """
        if not self.supports_line_reading:
            raise ValueError("Unsupported Operation on non-reading objects.")

        if not document.supports_line_writing:
            raise ValueError("The document does not support writing")

        document.add_lines(self.get_lines())
        return document

    def stream_from(self, document):
        """
        Streams from another document.

        Returns this document.
        """
        document.stream_to(self)
        return self

    def copy(self):
        """
        Copies the contents of the document
        """
        return self[:]

    def build(self):
        """
        Creates a build out of this document.
        """
        return DocumentBuilder(self, LineBuffer)

    def _support_line_reading(self):
        """
        Internal function: Return true if reading from this document is supported.
        """
        return False

    def _support_line_writing(self):
        """
        Internal function: Return true if writing to this document is supported.
        """
        return False

    def _support_styles(self):
        """
        Internal function: Return true if styles are supported in this document.
        """
        return False

    @property
    def supports_line_reading(self):
        """
        Checks if you can read lines from the document.
        """
        return self._support_line_reading()

    @property
    def supports_line_writing(self):
        """
        Checks if you can write lines to this document
        """
        return self._support_line_writing()

    @property
    def supports_styles(self):
        """
        Checks if this document supports styles.
        """
        return self._support_styles()

    def __iter__(self):
        """
        Iterates over all lines
        """
        if not self._support_line_reading():
            result = []
        else:
            result = self.get_lines()

        return iter(result)

    def __getitem__(self, index):
        """
        Returns an item or multiple items from the document.

        If the given item is a slice,
        there are three possibilities that depend on the values that are passed
        as slice:

        1) Strings or Time-Objects
           >>> Document['0:22:30.00':'0:22:40.00':]
           >>> Document[:Time('0:22:30.35')]

           Any slice index can be none as long start or stop is either a string or a
           time object.

           The times are clamped for the lines and syllables.
           Any syllable that would be outside the time range will be dropped.

           The step attribute has the same effect as if you would type
           >>> Document[start, stop][::step]

        2) Float as step:
           >>> Document[:25510:23.976]

           The start and stop value is treated as the frame number. The document will be
           sliced as if time-objects were passed. Step represents the fps.

           If start and/or stop is a string or a time-object, they will be used as
           time.

           If step is smaller than 0 or "NaN", the fps passed by the environment will be
           used.

        3) Anything other
           >>> Document[::]
           >>> Document[:]
           >>> Document[15:]
           >>> Document[:32:1]

           Slices according to the line indexes.

        """
        if isinstance(index, slice):
            return self._slice(index)

        return self._get_lines()[index].copy()

    def _slice(self, slice_obj):
        """
        There are multiple possibilities for slices.
        """
        start, stop, step = slice_obj.start, slice_obj.stop, slice_obj.step
        if (start is not None and isinstance(slice_obj, (basestring, Time))) or \
                (stop is not None and isinstance(slice_obj, (basestring, Time))):
            return self._slice_times(slice_obj)
        elif isinstance(slice_obj.step, float):
            return self._slice_frames(slice_obj)
        else:
            return self._slice_indices(start, stop, step)

    def _slice_indices(self, start, stop=None, step=None):
        """
        Slices according the line indexes.
        """
        if isinstance(start, slice):
            return LineBuffer(self.get_lines()[start])
        return self._slice_indices(slice(start, stop, step))

    def _slice_frames(self, slice_obj):
        """
        Slices according to the frame number.
        The step value will always be the fps number.
        """
        # Retrieve the fps from the viewport.
        if slice_obj.step <= 0 or slice_obj.step == float("nan"):
            fps = Viewport().fps

            if fps is None:
                raise RuntimeError("The FPS couldn't be retrieved")

        # Calculate the length of each frame.
        frame_length = 100/slice_obj.step

        start, stop = None, None
        # Convert start and stop to the actual time.
        if slice_obj.start is not None:
            if isinstance(slice_obj.start, (basestring, Time)):
                start = slice_obj.start
            else:
                start = frame_length*slice_obj.start
        if slice_obj.stop is not None:
            if isinstance(slice_obj.start, (basestring, Time)):
                stop = slice_obj.stop
            else:
                stop = frame_length*slice_obj.stop

        return self._slice_times(slice(start, stop, None))

    def _slice_times(self, slice_obj):
        """
        Slices according to the time
        """
        start, stop, step = slice_obj.start, slice_obj.stop, slice_obj.step

        # Convert string times to integers
        if isinstance(start, basestring):
            start = from_ass_time(start)
        if isinstance(stop, basestring):
            stop = from_ass_time(stop)

        # If stop is none set the stop to infinite.
        if stop is None:
            # Use infinite as end value.
            stop = float("inf")

        # If start is None, set the start to 0
        if start is None:
            start = 0

        if stop < start:
            raise ValueError("Stop must be grater than start.")

        result = []
        for line in self._get_lines():
            # If the line starts after the end of the slice.
            if line.start > stop or line.end < start:
                continue

            new_line = line.copy()
            new_line.text = line.text

            # Forcefully set start if the line starts before
            # the slice.
            if line.start < start:
                new_line.start = start

            # Forcefully set end if the line ends after the slice.
            if line.end > stop:
                new_line.end = stop

            new_syllables = []
            for syllable in line.syllables:
                # Also slice the syllables.
                if syllable.start > stop or syllable.end < start:
                    continue

                # Operate on a copy of the syllable.
                new_syllable = syllable.copy()

                # Clamp start.
                if syllable.start < start:
                    # Cache the end of the syllable.
                    syl_end = syllable.end

                    # Update the syllable.
                    new_syllable.start = start
                    new_syllable.end = syl_end

                # Clamp end.
                if syllable.end > stop:
                    new_syllable.end = stop

                new_syllables.append(new_syllable)

            # Update syllable list.
            new_line.syllables = new_syllables

            result.append(new_line)

        # Perform the step after the the times were calculated.
        if step is not None:
            result = result[::step]

        # Return the result as a new line-buffer.
        return LineBuffer(result)

    def __len__(self):
        """
        Returns the amount of lines in the
        """
        return len(self._get_lines())

    def __mul__(self, other):
        """
        Makes the lines appearing multiple times according to other.
        """
        def _mul(line):
            result = []
            for i in range(other):
                new_line = line.copy()
                new_line["original"] = line
                new_line["index"] = i
                new_line["count"] = other
                result.append(new_line)
            return result
        return self.refactor(_mul)


class LineBuffer(Document):
    """
    A buffer for lines.
    """

    def __init__(self, lines=None):
        if lines is not None:
            self.lines = lines
        else:
            self.lines = []

    def _support_line_writing(self):
        return True

    def _support_line_reading(self):
        return True

    def _get_lines(self):
        return self.lines

    def add_line(self, line):
        self.lines.append(line)

    def clear(self):
        """
        Clears the buffer.

        Terminal operation.
        """
        self.lines = []


class EnvironmentDocument(Document):
    """
    Base-Class for Documents using the environment.
    """

    def get_style_manager(self):
        """
        Returns the style manager of the environment.
        """
        return StyleManager.resolve()

    def get_environment(self):
        """
        Returns the environment the software is running under.
        """
        return get_environment()


class InputDocument(EnvironmentDocument):
    """
    Represents a document where you can read the lines from.
    """

    def __init__(self):
        self.lines = Line.get_lines()

    def _support_line_reading(self):
        """
        This document supports reading
        """
        return True

    def _support_styles(self):
        """
        This document supports styles if the environment supports styles.
        """
        return True

    def _get_lines(self):
        """
        Returns a copy of all lines.
        """
        return self.lines

    def get_styles(self):
        """
        Returns all styles if they are supported.
        """
        return StyleManager.resolve().get_styles()

    def get_style(self, name):
        """
        Returns the style with the given name.
        """
        return StyleManager.resolve().get_style(name)


class OutputDocument(EnvironmentDocument):
    """
    Represents a document for the output.
    """

    def __init__(self):
        self.environment = get_environment()

    def _support_line_writing(self):
        """
        This is a document to write to the main output of the environment
        """
        return True

    def add_line(self, line):
        self.environment.write_line(line)