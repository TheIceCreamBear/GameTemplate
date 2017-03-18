package com.joseph.gametemplate.threads;

import com.joseph.gametemplate.engine.GameEngine;
import com.joseph.gametemplate.gameobject.RenderLockObject;

public class RenderThread extends Thread {
	public RenderLockObject rlo;
	private GameEngine gEngine;
	public RenderThread(String name, RenderLockObject rlo, GameEngine ge) {
		super(name);
		this.rlo = rlo;
		this.gEngine = ge;
	}
	
	@Override
	public void run() {
		synchronized (rlo) {
			while (GameEngine.isRunning()) {
				try {
					rlo.wait();
					if (!rlo.wasNotified()) {
						continue;
					} else {
						gEngine.render();
						rlo.setWasNotified(false);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.exit(-1);
				}
			}
		}
	}
}