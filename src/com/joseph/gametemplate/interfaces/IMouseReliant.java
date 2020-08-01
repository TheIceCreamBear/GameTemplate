package com.joseph.gametemplate.interfaces;

import java.awt.event.MouseEvent;

/**
 * Specifies that the implementing object relies on mouse input
 * @author Joseph
 *
 */
public interface IMouseReliant {
	/**
	 * Called when a mouse even occurs
	 * @param e - the event
	 * @return a boolean weather or not this object used the mouse event
	 */
	public boolean onMouseEvent(MouseEvent e);
}