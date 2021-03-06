package com.joseph.gametemplate.handlers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.joseph.gametemplate.reference.Reference;

/**
 * GKELAH, or GlobalKeyEventListenerrAndHandler, is a key event handler that listens for all
 * key events and does a specific action based on the state of the engine and the key pressed.
 * Used for KeyStroke logging for text typing or for special key that must perform a specific 
 * action the moment they are pressed as opposed to waiting for the next update cycle of the 
 * object that will be using that special key.
 * 
 * <p>For legacy input, use {@link InputHandler InputHandler}.
 * 
 * @author Joseph
 * @see InputHandler
 */
public class GKELAH implements KeyListener {
	public GKELAH() {
		
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
		
		if (e.getKeyCode() == KeyEvent.VK_F1) {
			Reference.DEBUG_MODE = !Reference.DEBUG_MODE;
			return;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		
	}
}