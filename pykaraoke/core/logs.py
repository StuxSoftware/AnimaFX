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
Provides features for logging.
"""
import logging

from pykaraoke.core.environment import get_environment

__author__ = 'StuxCrystal'
__all__ = ["EnvironmentHandler", "get_logger"]


class EnvironmentHandler(logging.Handler):

    def __init__(self):
        super(EnvironmentHandler, self).__init__()
        self.environment = get_environment()

    def emit(self, record):
        if record.levelno == logging.NOTSET:
            record.levelno = logging.INFO

        level = self.environment.get_level_mapping()[record.levelno]
        time = record.created
        msg = record.getMessage()
        logger = record.name

        self.environment.log(level, msg, time, logger)


_logger = None
def get_internal_logger():
    """
    Returns the internal logger of PyKaraoke.
    """
    global _logger
    if _logger is None:
        _logger = logging.getLogger('PyKaraoke')
        _logger.addHandler(EnvironmentHandler())
        _logger.setLevel(get_environment().log_level)
    return _logger


def get_logger(name=None):
    if name:
        logger = logging.getLogger(name)
        logger.parent = get_internal_logger()
        return logger
    else:
        return get_internal_logger()