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
Useful operations during document annotations.
"""
import math
from environment import get_environment
__author__ = 'StuxCrystal'

__all__ = [
    "set_text", "set_value", "set_extension",
    "retime", "frame4frame"
]


def set_text(transformator):
    """
    Transforms the text.
    """
    def _set(line):
        line.text = transformator(line)
    return _set


def set_value(name, transformator):
    """
    Transforms the object.
    """
    def _set(line):
        setattr(line, name, transformator(line))
    return _set


def set_extension(transformer):
    """
    Modifies a extension variable
    """
    def _set(line):
        name, result = transformer(line)
        line[name] = result
    return _set


def retime(transformer):
    """
    Retimes the line. The result of the transformer is a tuple (absolute:bool, new_start:int, new_end:int)
    If new_start or new_end are None, they are ignored.
    """
    def _set(line):
        absolute, new_start, new_end = transformer(line)
        if absolute:
            new_start = line.start if new_start is None else new_start
            new_end = line.end if new_end is None else new_end
        else:
            new_start = (0 if new_start is None else new_start) + line.start
            new_end = (0 if new_end is None else new_end) + line.end

        line.start, line.end = new_start, new_end
    return _set

def frame4frame(fps=None, preferVideo=True):
    """
    Makes a frame4frame-sub out of the video.
    Does nothing if the fps couldn't be queried.
    """
    environment = get_environment()
    if fps is None and not environment.video_info_supported:
        return lambda line: [line]

    if fps is None or preferVideo:
        fps = environment.get_fps()

    def _refactor(line):
        cStart, count = line.start, math.ceil((line.end - line.start) / fps)
        result = []
        for i in range(count):
            cLine = line.copy()
            cLine.start = cStart
            cLine.end = min(cStart + 1/fps, line.end)
            cStart += 1/fps
            result["line"] = line
            result["index"] = i
            result["count"] = count
            result.append(result)
        return result
    return _refactor