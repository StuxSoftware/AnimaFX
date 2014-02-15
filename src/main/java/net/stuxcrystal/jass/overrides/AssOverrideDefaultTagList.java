/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 StuxCrystal
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.stuxcrystal.jass.overrides;

import net.stuxcrystal.jass.overrides.parsers.*;

import java.util.Arrays;
import java.util.List;

/**
 * Contains a list of the default tags of ASS.
 * 
 * @author StuxCrystal
 *
 */
public class AssOverrideDefaultTagList {
	
	/**
	 * Returns a list of the override types.
	 */
	public static final List<AssOverrideTagType> TYPES = Arrays.asList(
			
			new AssOverrideBooleanTag("i"),         // Italics
			new AssOverrideIntTag("b"),             // Bold
			new AssOverrideBooleanTag("u"),         // Underline
			new AssOverrideBooleanTag("s"),         // Strike-out
			
			new AssOverrideFloatTag("bord"),        // Border Size (Both)
			new AssOverrideFloatTag("xbord"),       // Border Size (X-Axis)
			new AssOverrideFloatTag("ybord"),       // Border Size (Y-Axis)
			
			new AssOverrideFloatTag("shad"),        // Shadow (Both)
			new AssOverrideFloatTag("xshad"),       // Shadow (X-Axis)
			new AssOverrideFloatTag("yshad"),       // Shadow (Y-Axis)
			
			new AssOverrideIntTag("be"),            // Blur Edges (Simple)
			new AssOverrideIntTag("blur"),          // Blur Edges (Gaussian Kernel)
			
			new AssOverrideStringTag("fn"),         // Font-Family
			new AssOverrideIntTag("fs"),            // Font-Size
			
			new AssOverrideIntTag("fscx"),          // Font-Scaling (X-Axis)
			new AssOverrideIntTag("fscy"),          // Font-Scaling (Y-Axis)
			
			new AssOverrideFloatTag("fsp"),         // Font-Spacing
			
			new AssOverrideFloatTag("fr"),          // Rotation (Z-Axis) => frz
			new AssOverrideFloatTag("frx"),         // Rotation (X-Axis)
			new AssOverrideFloatTag("fry"),         // Rotation (Y-Axis)
			new AssOverrideFloatTag("frz"),         // Rotation (Z-Axis) => fr
			
			new AssOverrideFloatTag("fax"),         // Text-Sharing (X-Axis)
			new AssOverrideFloatTag("fay"),         // Text-Sharing (Y-Axis)
			
			new AssOverrideIntTag("fe"),            // Font-Encoding
			
			new AssOverrideColorTag("c"),           // Color (Primary) => 1c
			new AssOverrideColorTag("1c"),          // Color (Primary) => c
			new AssOverrideColorTag("2c"),          // Color (Secondary)
			new AssOverrideColorTag("3c"),          // Color (Outline)
			new AssOverrideColorTag("4c"),          // Color (Shadow)
			
			new AssOverrideAlphaTag("alpha"),       // Alpha (Primary) => 1a
			new AssOverrideAlphaTag("1a"),          // Alpha (Primary) => alpha
			new AssOverrideAlphaTag("2a"),          // Alpha (Secondary)
			new AssOverrideAlphaTag("3a"),          // Alpha (Outline)
			new AssOverrideAlphaTag("4a"),          // Alpha (Shadow)
			
			new AssOverrideIntTag("an"),            // Line Alignment (ASS)
			new AssOverrideIntTag("a"),             // Line Alignment (SSA)
			
			new AssOverrideIntTag("k"),             // Karaoke (Secondary->Primary on HL Start)
			new AssOverrideIntTag("K"),             // Karaoke (Primary fills Secondary while HL) => kf
			new AssOverrideIntTag("kf"),            // Karaoke (Primary fills Secondary while HL) => K
			new AssOverrideIntTag("ko"),            // Karaoke (Border/Shadow on HL-Start)
			new AssOverrideIntTag("kt"),            // Karaoke (Set-Timing) ASS2 @Deprecated
			
			new AssOverrideIntTag("q"),             // Wrap-Style
			
			new AssOverrideStringTag("r"),          // Reset to style.
			
			new AssOverridePositionTag("pos"),      // Sets the position
		    new AssOverrideMovementTag("move"),     // Moves the entire line.
			
			new AssOverridePositionTag("org"),      // Rotation origin.
			
			new AssOverrideFadeTag("fad", true),    // Simple Fade Tag.
			new AssOverrideFadeTag("fade", false),  // Complex Fade Tag.
			
			new AssOverrideTransitionTag("t"),      // The transitions.
			
			new AssOverrideClipTag("clip", false),  // The normal clip.
			new AssOverrideClipTag("iclip", true),  // the inversed clip.
			
			new AssOverrideIntTag("p"),             // Drawings
            new AssOverrideIntTag("pbo")            // Base-Line-Offset for Drawings.
	);
	
	public static void addDefaultTags(AssOverrideTagList tags) {
		for (AssOverrideTagType type : TYPES)
            tags.put(type);
	}

}
