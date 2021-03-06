package com.joseph.gametemplate.reference;

import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.Hashtable;
import java.util.Map;

/**
 * Commonly used Objects or primitives stored in one location for quick Reference
 * 
 * @author Joseph
 *
 */
public class Reference {
	public static final String DIRPREFIX = System.getProperty("user.dir");
	public static boolean DEBUG_MODE = true;
	public static boolean HARD_CORE_DEBUG_MODE = false;
	
	public static class Fonts {
		private static Map<TextAttribute, Object> map;
		public static void init() {		
			map = new Hashtable<TextAttribute, Object>();
			map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
			DEFAULT_UNDERLINED_FONT = DEFAULT_FONT.deriveFont(map);
			SCALED_UP_UNDERLINED_FONT = SCALED_UP_FONT.deriveFont(map);
		}
		
		public static final Font DEFAULT_FONT = new Font("Consolas", 0, 20);
		public static final Font SCALED_UP_FONT = new Font("Consolas", 0, 40);
		public static Font DEFAULT_UNDERLINED_FONT;
		public static Font SCALED_UP_UNDERLINED_FONT;
	}
}