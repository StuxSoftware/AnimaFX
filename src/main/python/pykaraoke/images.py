"""
Class to support images.
"""
from environment import get_environment
from types import Line
from document import Document

__author__ = 'StuxCrystal'

#
# This is a simple shape to support pixels.
#
PIXEL_SHAPE = "m 0 0 l 0 1 1 1 1 0"


class Image(Document):
    """
    Represents an image.

    Please note that all lines will assume {\an5} when calculating its position.
    Their width and height however will always be 1. Their start and end-time will
    always be 0.

    Each object will have a color in the RRGGBB form as well as its alpha value and
    its position.
    """

    def __init__(self, name, style):
        self.name = name
        self.style = style
        self.pixels = None

    def _support_line_reading(self):
        """
        Images are containers.
        The result of this function is True if the environment
        supports images. Otherwise this function will return False.
        """
        return get_environment().images_supported

    def _get_pixels(self):
        """
        Returns all pixels of the file or an
        empty array if the file could not be retrieved.
        """
        image = get_environment().get_image(self.name)
        if image is None:
            # Empty array.
            return []

        result = []
        for x, row in enumerate(image):
            for y, pixel in enumerate(row):
                result.append(Line(0, 0, self.style, 5, text=PIXEL_SHAPE, extensions={
                    x: x, y: y, width: 1, height: 1,
                    color: pixel | 0xFF000000,
                    alpha: pixel & 0xFF000000 >> 24
                }))
        return result

    def _get_lines(self):
        if self.pixels is None:
            self.pixels = self._get_pixels()
        return self.pixels