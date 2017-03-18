package com.joseph.gametemplate.gameobject;

import com.joseph.gametemplate.engine.GameEngine;
import com.joseph.gametemplate.interfaces.IDrawable;
import com.joseph.gametemplate.interfaces.IUpdateable;

/**
 * Generic GameObject that is both updateable and drawable
 * 
 * @author David Santamaria
 *
 */
public abstract class GameObject implements IDrawable, IUpdateable {
	protected boolean[] isKeyPresed = GameEngine.isKeyPressed;
}