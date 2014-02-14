"""
Returns common types of karabuilder.
"""
from .environment import get_environment

__author__ = 'StuxCrystal'

class _Viewport(object):
    """
    Viewports.
    """

    def __init__(self):
        """
        Internal object.
        """
        self.environment = get_environment()

    @property
    def resolution(self):
        """
        Returns the resolution.
        """
        return self.environment.get_viewport_size()

    @property
    def lines(self):
        """
        Returns a line.
        """
        return Line.get_lines()

    @property
    def styles(self):
        """
        Returns all styles.
        """
        return Style.get_all_styles()


class Style(object):
    """
    Represents a simple style.
    """

    def __init__(self, name, font, size, italic, bold, spacing=0):
        """
        Creates a new style object.
        """
        self.name = name
        self.font = font
        self.size = size
        self.italic = italic
        self.bold = bold
        self.spacing = 0

    def text_extents(self, text):
        """
        Returns the text-extents of the given style.
        """
        environment = get_environment()

        # If the environment does support styles,
        # check if the meta-data equals each other and if so, let the environment calculate
        # the values using the name of the style only.
        if environment.styles_supported:
            orig = Style.get_style(self.name)

            # Check if the styles equal each other.
            if orig is not None and self == orig:
                # If so, only use the style name.
                return environment.text_extents(self.name, text)

        # Use a dictionary if styles are not supported or
        return environment.text_extents(self._to_dict(), text)

    def copy(self):
        """
        Copies the style.
        """
        return Style(**self._to_dict())

    def __repr__(self):
        return "<Style '%s' font:'%s' size:%d bold:%r italic:%r>" % (self.name, self.font, self.size, self.bold, self.italic)

    def _to_dict(self):
        """
        Transforms the style to the internal dictionary type of the style.
        """
        return {"name": self.name, "font": self.font, "size": self.size, "bold": self.bold, "italic": self.italic}

    def __eq__(self, other):
        """
        Checks if both styles are equal.
        """
        if other is None or not isinstance(other, Style):
            return False
        return self._to_dict() == other._to_dict()

    @staticmethod
    def get_all_styles():
        """
        Returns all styles inside the environment. Returns a empty dictionary if styles are not supported by
        the underlying environment.
        """

        environment = get_environment()

        # Do not return anything if the environment does not store styles.
        if not environment.styles_supported:
            return {}

        # Returns a dictionary of all supported styles.
        result = {}
        for name, data in environment.get_styles().items():
            result[name] = Style(name=name, **data)
        return result

    @staticmethod
    def get_style(name):
        """
        Returns the style of the environment. Returns None if styles are not supported by the underlying
        environment.
        """

        environment = get_environment()

        # Do not return anything if the environment does not store styles.
        if not environment.styles_supported:
            return None

        # Retrieve the style and return None if the style does not exist.
        data = environment.get_style(name)
        if data is None:
            return None

        # Make this thing a style object.
        return Style(name=name, **data)


class ExtensibleObject(object):
    """
    Represents a extensible object.
    """

    def __init__(self):
        self.extension = {}

    def __getitem__(self, item):
        return self.extension[item]

    def __setitem__(self, key, value):
        self.extension[key] = value

    def __delitem__(self, key):
        del self.extension[key]

    def __contains__(self, item):
        return item in self.extension

    def _copy(self):
        return self.extension.copy()


class Syllable(ExtensibleObject):
    """
    Stores the data of the syllable.
    """

    def __init__(self, line, start, duration, text):
        """
        Creates a new syllable.
        """
        ExtensibleObject.__init__(self)
        self.line = line
        self.start = start
        self.duration = duration
        self.text = text

    def copy(self):
        result = Syllable(self.line, self.start, self.duration, self.text)
        result.extension = self._copy()
        return result

    def __repr__(self):
        return "<Syllable start:%d duration:%d text:'%s' extension:%s>" % (self.start, self.duration, self.text, self.extension)

    @property
    def end(self):
        return self.start + self.duration*10


class Line(ExtensibleObject):
    """
    Stores the data of the syllable.
    """

    def __init__(self, start, end, style, anchor, margin=(10,10,10), layer=0, text=None, extensions={}):
        """
        Creates a new line.
        """
        ExtensibleObject.__init__(self)
        self.start = start
        self.end = end
        self.style = self._get_style(style)
        self.margin = margin
        self.anchor = anchor
        self.layer = layer
        self.raw_text = text
        self.syllables = []
        self.extension = extensions

    def write(self):
        get_environment().write_line(self)

    def copy(self):
        result = Line(self.start, self.end, self.style.copy(), self.margin, self.anchor, self.layer)
        result.syllables = [syllable.copy() for syllable in self.syllables]
        result.raw_text = self.raw_text
        return result

    @property
    def duration(self):
        """
        Sets the duration.
        """
        return self.end - self.start

    @property
    def text(self):
        if self.raw_text is not None:
            return self.raw_text

        return "".join([syllable.text for syllable in self.syllables])

    @text.setter
    def text(self, value):
        self.raw_text = value

    def __repr__(self):
        return "<Line start:%d end:%d style:%r margin:%r anchor:%d %s extensions:%r layer:%i>" % (
            self.start, self.end, self.style, self.margin, self.anchor,
            ("syllables:%r" % self.syllables if self.raw_text is None else "text:'%s'" % self.raw_text),
            self.extension, self.layer
        )

    @staticmethod
    def _get_style(style):
        """
        Returns the style.
        """
        if isinstance(style, Style):
            return style

        if isinstance(style, basestring):
            return Style.get_style(style)
        return Style(**style)

    @staticmethod
    def get_lines():
        """
        Returns all lines.
        """
        environment = get_environment()

        result = []
        for rawline in environment.get_syllables():
            line = Line(**rawline[0])
            for syllable in rawline[1:]:
                line.syllables.append(Syllable(line=line, **syllable))
            result.append(line)
        return result

#######################################################################################################################
# The internal viewport.
_viewport = None


def get_viewport():
    """
    Returns the viewport.
    """
    global _viewport
    if _viewport is None:
        _viewport = _Viewport()
    return _viewport