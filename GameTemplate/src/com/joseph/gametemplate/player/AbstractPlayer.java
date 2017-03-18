package com.joseph.gametemplate.player;

import com.joseph.gametemplate.gameobject.GameObject;

/**
 * An abstract player class that is used as a foundation to create other player
 * classes
 * 
 * @author David Santamaria
 *
 */
public abstract class AbstractPlayer extends GameObject {
	protected enum EnumDirection {
		UP, DOWN, LEFT, RIGHT;
	}
}