package com.joseph.gametemplate.gui;

import java.awt.Point;

import com.joseph.gametemplate.interfaces.IMouseReliant;
import com.joseph.gametemplate.reference.ScreenReference;

/**
 * Extension of GuiElement that acts as a clickable button
 * @author Joseph
 * @see IGuiElement
 *
 */
public abstract class AbstractButton extends GuiElement implements IMouseReliant {	
	public AbstractButton(int x, int y, int width, int height, boolean scaled) {
		super(x, y, width, height, scaled);
		this.visible = true;
	}

	@Override
	public boolean isMouseInElement() {
		Point p = ScreenReference.getMouseLocation();
		if (p == null) {
			return false;
		}
		return p.x >= x && p.x <= (x + width) && p.y >= y && p.y <= (y + height);
	}
}