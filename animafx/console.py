#
# AnimaFX
# Copyright (C) 2014 StuxCrystal
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#
"""
Interactive console for AnimaFX.
"""
__author__ = 'StuxCrystal'

import code
import tokenize
import traceback

from animafx.version import __version__ as version

class CodeHandler(object):

    def execute(self):
        raise NotImplementedError

    @staticmethod
    def get_handler(script=None):
        if script is None:
            return ConsoleHandler()
        else:
            return FileHandler(script)


class ConsoleHandler(object):

    def __init__(self):
        self.interp = code.InteractiveConsole({'__name__': '__console__', '__doc__': None}, '<AnimaFX IC>')

    def execute(self):
        try:
            self.interp.interact("AnimaFX %s\nPython Interactive Console"%version)
        except SystemExit as e:
            raise e
        except BaseException as e:
            traceback.print_exception(type(e), e, e.__traceback__)
            return False
        return True

class FileHandler(object):

    def __init__(self, script):
        self.script = script

    def execute(self):
        # Try to detect the encoding for you.
        with open(self.script, 'rb') as file:
            try:
                encoding = tokenize.detect_encoding(file.readline)[0]
            except SyntaxError:
                encoding = "utf-8"

        # Set the global values for the module.
        global_values = {
            '__file__': self.script,       # Use actual filename of the script.
            '__name__': '__main__'         # Make sure that 'if __name__ == "__main__"'-hook works
        }

        with open(self.script, 'r', encoding=encoding) as file:
            # Do not inherit any 'from future import ...'-statements that may be used
            # by AnimaFX.
            # Additionally set the current filename.
            module = compile(file.read(), self.script, 'exec', False)

        try:
            exec(module, global_values)
        # Reraise any occuring exceptions
        except (SystemExit, KeyboardInterrupt) as e:
            raise e

        # Print the exception
        except BaseException as e:
            traceback.print_exception(e.__class__, e, e.__traceback__)
            return False

        return True