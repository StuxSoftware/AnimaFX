"""
Null object pattern.

Used when ``None`` cannot be used.
"""

class Null(object):
    """
    This is the null-object. It is a singleton.
    """

    _instance = None

    def __new__(cls):
        if cls._instance is None:
            cls._instance = super(Null, cls).__new__(cls)
        return cls._instance

    def __setattr__(self, key, value):
        raise AttributeError("You cannot change '%s'.", self.__class__.__name__)

null = Null()