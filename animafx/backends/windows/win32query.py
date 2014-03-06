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
import ctypes
from ctypes import wintypes
from cext import Structure, SharedLibrary, CFunction

MULTIPLIER = 1  # 64.0


class LogFont(Structure):
    """
    Implementation for the LOGFONTW Structure.
    """
    lfHeight = wintypes.LONG
    lfWidth = wintypes.LONG
    lfEscapement = wintypes.LONG
    lfOrientation = wintypes.LONG
    lfWeight = wintypes.LONG
    lfItalic = wintypes.BYTE
    lfUnderline = wintypes.BYTE
    lfStrikeOut = wintypes.BYTE
    lfCharSet = wintypes.BYTE
    lfOutPrecision = wintypes.BYTE
    lfClipPrecision = wintypes.BYTE
    lfQuality = wintypes.BYTE
    lfPitchAndFamily = wintypes.BYTE
    lfFaceName = wintypes.WCHAR * 32


class TextMetric(Structure):
    """
    Implemenation for the TextMetric Structure
    """
    tmHeight = wintypes.LONG
    tmAscent = wintypes.LONG
    tmDescent = wintypes.LONG
    tmInternalLeading = wintypes.LONG
    tmExternalLeading = wintypes.LONG
    tmAveCharWidth = wintypes.LONG
    tmMaxCharWidth = wintypes.LONG
    tmWeight = wintypes.LONG
    tmOverhang = wintypes.LONG
    tmDigitizedAspectX = wintypes.LONG
    tmDigitizedAspectY = wintypes.LONG
    tmFirstChar = wintypes.WCHAR
    tmLastChar = wintypes.WCHAR
    tmDefaultChar = wintypes.WCHAR
    tmBreakChar = wintypes.WCHAR
    tmItalic = wintypes.BYTE
    tmUnderlined = wintypes.BYTE
    tmStruckOut = wintypes.BYTE
    tmPitchAndFamily = wintypes.BYTE
    tmCharSet = wintypes.BYTE


class MSVCRT(SharedLibrary):
    _name_ = "msvcrt"

    strlen = CFunction("wcslen", arg_types=(ctypes.c_wchar_p, ))


class GDI32(SharedLibrary):
    _loader_ = ctypes.WinDLL
    _name_ = "gdi32"

    _CreateCompatibleDC = CFunction("CreateCompatibleDC")
    DeleteObject = CFunction(arg_types=(ctypes.c_void_p, ))
    _CreateFontIndirect = CFunction("CreateFontIndirectW")
    SelectObject = CFunction()
    _GetTextExtentPoint32 = CFunction("GetTextExtentPoint32W")
    _GetTextMetrics = CFunction("GetTextMetricsW")
    SetMapMode = CFunction("SetMapMode", arg_types=(wintypes.HDC, ctypes.c_int))

    def CreateCompatibleDC(self, hdc=0):
        """
        Creates a compatible DC.
        """
        return self._CreateCompatibleDC(hdc)

    def CreateFontIndirect(self, logfont):
        """
        Creates a new font
        """
        return self._CreateFontIndirect(ctypes.pointer(logfont))

    def GetTextExtentPoint32(self, hdc, str):
        """
        Returns the text-extent-point
        """
        result = wintypes.SIZE()
        buf = ctypes.create_unicode_buffer(str)
        if not self._GetTextExtentPoint32(hdc, buf, MSVCRT.strlen(buf), ctypes.pointer(result)):
            raise WindowsError("Failed to get TextExtents")
        return result

    def GetTextMetrics(self, dc):
        result = TextMetric()
        if not self._GetTextMetrics(dc, ctypes.pointer(result)):
            raise WindowsError("Failed to get TextMetrics.")
        return result


class Handle(object):

    def __init__(self, data, free=lambda free: None):
        self.data = data
        self.free = free

    def __enter__(self):
        return self.data

    def __exit__(self, exc_type, exc_val, exc_tb):
        self.free(self.data)
        return False


def calculate_text_extents(style, text):
    """
    Calculates the text-extents.
    <-- style:{font:str, size:int, bold:bool, italic:bool[, name:str] }, text:str
    --> {width:float, height:float, ascent:float, descent:float, extlead:float, intlead:float}
    Note that height is optional if ascent and descent are given.
    """
    gdi32 = GDI32()

    with Handle(gdi32.CreateCompatibleDC(), gdi32.DeleteObject) as dc:
        gdi32.SetMapMode(dc, 1)

        logfont = LogFont()
        logfont.lfHeight = int(style['size'] * MULTIPLIER)
        logfont.lfWeight = 700 if style['bold'] else 400       # FW_BOLD if style['bold'] else FW_NORMAL
        logfont.lfItalic = style['italic']
        logfont.lfUnderline = False
        logfont.lfStrikeOut = False
        logfont.lfCharSet = 1                                  # DEFAULT_CHARSET
        logfont.lfOutPrecision = 4                             # OUT_TT_PRECIS
        logfont.lfClipPrecision = 0                            # CLIP_DEFAULT_PRECIS
        logfont.lfQuality = 4                                  # ANTIALIASED_QUALITY
        logfont.lfPitchAndFamily = 0 | 0                       # DEFAULT_PITCH | FF_DONTCARE
        logfont.lfFaceName = style['font']

        with Handle(gdi32.CreateFontIndirect(logfont), gdi32.DeleteObject) as font:
            gdi32.SelectObject(dc, font)
            size = gdi32.GetTextExtentPoint32(dc, text)
            width, height = size.cx, size.cy

            tm = gdi32.GetTextMetrics(dc)
            ascent, descent, extlead, intlead = tm.tmAscent, tm.tmDescent, tm.tmExternalLeading, tm.tmInternalLeading

    result = {
        "width": width/MULTIPLIER, "height": height/MULTIPLIER,
        "ascent": ascent/MULTIPLIER, "descent": descent/MULTIPLIER,
        "extlead": extlead/MULTIPLIER, "intlead": intlead/MULTIPLIER
    }

    return result