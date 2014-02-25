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
import sys
import tokenize
import traceback

import argparse

from .environment import AnimaFXEnvironment
from .singleton import Singleton
__author__ = 'StuxCrystal'


class AnimaFX(object):
    """
    The Base-Class of AnimaFX.
    """

    __metaclass__ = Singleton

    def __init__(self, instream, outstream, script):
        """
        <-- instream:fd, outstream:fd, script:str
        """
        self.instream = instream
        self.outstream = outstream
        self.script = script

        self.environment = None

    def run(self):
        self.start()
        if self.execfile():
            self.stop()

    def start(self):
        self.environment = AnimaFXEnvironment(self)
        self.environment.feed(self.instream)

    def stop(self):
        self.environment.dump(self.outstream)

    def execfile(self):
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
        # except BaseException as e:
        #     traceback.print_exception(e.__class__, e, e.__traceback__)
        #     return False

        return True

    @classmethod
    def main(cls, args = ("animafx.py",)):
        parser = argparse.ArgumentParser(
            description="Free, Open-Source and Platform-Independent Karaoke-FX Generator.",
            epilog="Be independent."
        )

        parser.add_argument("-s", "--script", help="The script", required=True, type=str)
        parser.add_argument("-i", "--input", help="The input file", type=argparse.FileType("rb"), default=sys.stdin)
        parser.add_argument("-o", "--output", help="The output file", type=argparse.FileType("w"), default=sys.stdout)

        parsed = parser.parse_args(args)

        animafx = cls(parsed.input, parsed.output, parsed.script)
        try:
            animafx.run()
        except BaseException as e:
            traceback.print_exception(e.__class__, e, e.__traceback__)
            return 1

        return 0

if "__name__" == "__main__":
    sys.exit(AnimaFX.main(sys.argv))