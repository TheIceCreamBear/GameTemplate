package com.joseph.gametemplate.player;

import com.joseph.gametemplate.gameobject.GameObject;

public abstract class AbstractPlayer extends GameObject {
	protected enum EnumDirection {
		UP,
		DOWN,
		LEFT,
		RIGHT;
	}
}