package com.joseph.gametemplate.gameobject;

import com.joseph.gametemplate.engine.GameEngine;
import com.joseph.gametemplate.interfaces.IDrawable;
import com.joseph.gametemplate.interfaces.IUpdateable;

public abstract class GameObject implements IDrawable, IUpdateable {
	protected boolean[] isKeyPresed;
	
	public GameObject() {
		this.isKeyPresed = GameEngine.getKeyPressedArray();
	}
}
