package com.joseph.gametemplate.gameobject;

/**
 * Some weird stuff idk how it works, because Joseph did it
 * 
 * @author David Santamaria
 *
 */
public class RenderLockObject {
	private boolean wasNotified;

	public RenderLockObject() {
		this.wasNotified = false;
	}

	public void setWasNotified(boolean wasNotified) {
		this.wasNotified = wasNotified;
	}

	public boolean wasNotified() {
		return wasNotified;
	}
}