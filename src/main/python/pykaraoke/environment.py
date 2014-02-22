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
Defines the environment for karabuilder.

"""
from utils import to_ass_time
__author__ = 'Stux'
__all__ = [
    # Exceptions
    "UnsupportedOperationException", "EnvironmentRedefinitionException",

    # Environments
    "Environment", "EmbeddedEnvironment",

    # Retrieval of Environments.
    "get_environment", "set_environment"
]

# Instance of the current environment.
_environment = None


class UnsupportedOperationException(NotImplementedError):
    """
    If the operation is not supported, this exception is thrown.
    """
    pass


class EnvironmentRedefinitionException(Exception):
    """
    Thrown if the current environment is being redefined.
    """
    pass


class Environment(object):
    """
    This is the base class for environments.
    """
    def __init__(self, support, attributes):
        self.support = support
        self.attributes = attributes

    def text_extents(self, style, text):
        # Since we need the spacing, we need to resolve the
        # style if only the name of the style is given.
        if isinstance(style, basestring):
            if "styles" not in self.support:
                raise UnsupportedOperationException("Styles are not supported.")
            style_data = self.get_style(style)
        else:
            style_data = style

        # Resolve the spacing.
        # Default to 0 if no spacing is given.
        if "spacing" not in style_data:
            spacing = 0
        else:
            spacing = style_data["spacing"]

        # Check if spacing is grater than 0.
        if spacing > 0:
            # Calculate the width manually.
            width = 0
            extents = {}

            # Calculate the string bounds for
            for char in text:
                data = self._text_extents(style, char)
                extents.update(data)
                width += data["width"] + spacing
            else:
                # If the text is empty, enforce the calculation of the
                # text width.
                extents.update(self._text_extents(style, ""))
            extents["width"] = width

        # If not, just calculate the extents.
        else:
            extents = self._text_extents(style, text)

        return extents

    def _text_extents(self, style, text):
        raise NotImplemented

    def get_syllables(self):
        raise NotImplemented

    def get_viewport_size(self):
        raise NotImplemented

    def get_image(self, filename):
        raise UnsupportedOperationException

    def get_style(self, name):
        raise UnsupportedOperationException

    def get_styles(self):
        raise UnsupportedOperationException

    def write_line(self, line):
        print "Dialogue: %(layer)i,%(start)s,%(end)s,%(style)s,,%(ml)d,%(mr)d,%(mv)d,,%(text)s" % (
            {
                "layer": line.layer,
                "start": to_ass_time(line.start), "end": to_ass_time(line.end),
                "style": line.style.name,
                "text": line.text,
            }.update(map(lambda a, b: (a, b), ("ml", "mr", "mv"), line.margin))
        )

    def get_fps(self):
        return None

    def _get_style_manager(self):
        raise UnsupportedOperationException

    @property
    def stylemanager(self):
        if "stylemgr" not in self.attributes:
            return None
        return self._get_style_manager()

    @property
    def images_supported(self):
        return "images" in self.support

    @property
    def styles_supported(self):
        return "styles" in self.support

    @property
    def needs_full_generation(self):
        """
        Checks if KaraBuilder needs to generate a complete ASS-File.
        """
        return "output" not in self.support

    @property
    def video_info_supported(self):
        """
        Can you query meta-data about the video.
        """
        return "vinfo" in self.styles_supported


class EmbeddedEnvironment(Environment):
    """
    The default environment uses a module "_karabuilder_environment" that has to be
    provided by the underlying system.

    Each "_environment"-Module has to support at least the following functions:
    | "_environment.name"
    | | The name of the environment.
    |
    | "_environment.support"
    | | This attribute is a tuple of strings containing of all supported extensions to pykaraoke.
    | | These strings are currently supported
    | | > "images" : If the environments supports images.
    | | > "styles" : Supports styles.
    | | > "output" : Uses an external output file. (That means that kara-build will not write into stdout.)
    | | > "vinfo"  : If the environment has meta-data about the video (e.g. fps)
    |
    | "_environment.attributes"
    | | This attribute is a tuple of strings containing all flags of the environment. This can either be
    | | bugs that PyKaraoke can work around with or specific properties of some callback functions.
    | | > "stylemgr"       : This environment has its own style manager. Use "environment.stylemanager"
    | |                      to get the style manager.
    | |                      (Only supported in non embedded environments)
    |
    | "_environment.text_extents(dict[font=None, size=0, bold=False, italic=False] or stylename, str:text)
    | | Returns the boundary of the given string. (type: dict)
    | | Keys:
    | | > "width", "height" (required)
    | |   The size of the string.
    | | > "ascend", "descent" (both are optional)
    | |   The ascent and descent.
    | | > "extlead", "intlead" (both are optional) (The leading <external and internal> of the text)
    | |   The leading of the line.
    | | > "advance" (optional) [only for each char]
    | |   The amount of pixels where the environment would set the next character.
    |
    | "_environment.get_syllables()"
    | | Returns a two dimensional array of syllables.
    | | Each array inside the list represents each line. The first element of the array are metadata about
    | | the line.
    | |
    | | The function may be called multiple times.
    | |
    | | The metadata about the line.
    | | > {"start":int, "end":int,
    | |    "style":{font:str, size:int, bold:bool, italic:bool, name:str} or Style-Name (if styles are supported),
    | |    "anchor": ASS-Anchor-Integer value.,
    | |    "margin": (l,r,v),
    | |    "layer":int
    | |   }
    | |
    | | Each other element are the data of the syllable:
    | | > {"start":int (relative to line start), "duration":int, "text":str}
    |
    | "_environment.get_viewport_size()"
    | | Returns an array (width, height) with the size of the viewport (aka. X-Resoultion and Y-Resolution)

    If "images" is in "_environment.support()"
    | "_environment.get_image(str:filename)"
    | | This functions returns a two-dimensional array of pixels.
    | | Each element contains a integer with the color (and alpha).
    | | This integer has the form 0xRRGGBBAA.

    If "styles" is in "_environment.support()"
    | "_environment.get_style(str:stylename)
    | | Returns a dict with these types:
    | | > {"font":str, "size":int, "bold":boolean, "italic":boolean, ["spacing":int=0]}
    |
    | "_environment.get_styles()"
    | | Returns a dict with the style name as key and another dictionary with the format
    | | > {"font":str, "size":int, "bold":boolean, "italic":boolean}
    | | as value.

    If "output" is in "_environment.support()"
    | "_environment.write_line(str/dict for style, str:text, int:start, int:end, int:layer)"
    | | Writes a new line into the output.

    If "vinfo" is in "_environment.support()"
    | "_environment.get_fps()"
    | | Returns a float containing the fps.
    """
    def __init__(self):
        try:
            import _karabuilder_environment
        except ImportError:
            raise UnsupportedOperationException("The underlying software does not seem to support PyKaraoke.")

        self.module = _karabuilder_environment
        Environment.__init__(self, self.module.support, self.module.attributes)

    def _text_extents(self, style, text):
        """
        Returns the boundary of the text.
        """
        if isinstance(style, str) and "styles" not in self.support:
            raise UnsupportedOperationException("Styles are not supported by the environment.")
        return self.module.text_extents(style, text)

    def get_syllables(self):
        """
        Returns the syllables of the text.
        """
        return self.module.get_syllables()

    def get_image(self, filename):
        """
        Returns the image of the value.

        May not be supported by the environment
        """
        if "images" not in self.support:
            raise UnsupportedOperationException("Images are not supported by the environment.")
        return self.module.get_image(filename)

    def get_style(self, name):
        """
        Returns the styles of the environment.

        May not be supported by the environment.
        """
        if "styles" not in self.support:
            raise UnsupportedOperationException("Styles are not supported by the environment.")

        return self.module.get_style(name)

    def get_styles(self):
        """
        Returns all styles of the script.

        May not be supported by the environment.
        """
        if "styles" not in self.support:
            raise UnsupportedOperationException("Styles are not supported by the environment.")

        return self.module.get_styles()

    def get_viewport_size(self):
        """
        Returns the size of the viewport.
        """
        return self.module.get_viewport_size()

    def write_line(self, line):
        """
        Writes a line into the environment.
        """
        if "output" in self.support:
            if "styles" in self.support:
                style = line.style.name
            else:
                style = {
                    "font": line.style.font,
                    "size": line.style.size,
                    "bold": line.style.bold,
                    "italic": line.style.italic
                }

            self.module.write_line(style, line.text, line.start, line.end, line.layer)
        else:
            # Pass to the base method of the environment.
            Environment.write_line(self, line)

    def get_fps(self):
        """
        Queries the fps.
        """
        if "vinfo" in self.support:
            return self.module.get_fps()
        return None

    def _get_style_manager(self):
        # Not supported in EmbeddedEnvironments.
        return None

    def __repr__(self):
        return "<EmbeddedEnvironment: " + self.module.name + ">"


def set_environment(environment):
    """
    Sets the current environment.
    """
    global _environment
    if _environment is not None:
        raise EnvironmentRedefinitionException("You cannot redefine the current environment.")
    _environment = environment

def get_environment():
    """
    Returns the current environment
    """
    if _environment is None:
        set_environment(EmbeddedEnvironment())
    return _environment