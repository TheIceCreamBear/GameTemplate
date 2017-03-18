package com.joseph.gametemplate.engine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import com.joseph.gametemplate.gameobject.GameObject;
import com.joseph.gametemplate.gameobject.RenderLockObject;
import com.joseph.gametemplate.gui.IGuiOverlay;
import com.joseph.gametemplate.interfaces.IDrawable;
import com.joseph.gametemplate.interfaces.IUpdateable;
import com.joseph.gametemplate.refrence.Refrence;
import com.joseph.gametemplate.screen.Screen;
import com.joseph.gametemplate.threads.RenderThread;
import com.joseph.gametemplate.threads.ShutdownThread;

/**
 * @author David Santamaria - Original Author
 * @author Joseph Terribile - Current Maintainer
 */
public class GameEngine {
	private static boolean running = true;
	private static Random rand;
	private static GameEngine instance;
	private static String stats = "";

	private JFrame frame;
	private Graphics g;
	private Graphics g2;
	private BufferedImage i;

	private RenderLockObject rlo;
	private RenderThread rtInstance;
	private ShutdownThread sdtInstance;

	/* The three types of Game Objects */
	private static ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
	private static ArrayList<IUpdateable> updateable = new ArrayList<IUpdateable>();
	private static ArrayList<IDrawable> drawable = new ArrayList<IDrawable>();
	private static ArrayList<IGuiOverlay> guiOverlays = new ArrayList<IGuiOverlay>();

	private static boolean[] isKeyPressed = new boolean[256];
	
	public static boolean[] getKeyPressedArray() {
		return isKeyPressed;
	}

	public static GameEngine getInstance() {
		return instance;
	}

	public static Random getRand() {
		return rand;
	}

	public static boolean isRunning() {
		return running;
	}

	public static void main(String[] args) {
		if (Refrence.DEBUG_MODE) {
			System.out.println(Runtime.getRuntime().maxMemory());
			System.err.println("x: " + Screen.width + "y: " + Screen.height);
		}
		instance = new GameEngine();
		instance.run();
	}

	public static void startGameEngine() {
		instance = new GameEngine();
		instance.run();
	}

	public GameEngine() {
		initialize();
	}

	public KeyListener getKeyListener() {
		return new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				isKeyPressed[e.getKeyCode()] = true;
			}

			@Override
			public void keyReleased(KeyEvent e) {
				isKeyPressed[e.getKeyCode()] = false;
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO OOOOOOO
			}

		};
	}

	public void reinitialize() {

	}

	public void initialize() {
		this.sdtInstance = new ShutdownThread();
		Runtime.getRuntime().addShutdownHook(sdtInstance);

		rand = new Random();

		this.frame = new JFrame("Game Template");
		this.frame.setBounds(0, 0, Screen.width, Screen.height);
		this.frame.setResizable(false);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setVisible(true);

		this.rlo = new RenderLockObject();
		this.rtInstance = new RenderThread("RenderThread", this.rlo, this);
		this.rtInstance.start();

		this.i = new BufferedImage(Screen.width, Screen.height, BufferedImage.TYPE_INT_RGB);
		this.g2 = this.i.createGraphics();
		this.g = frame.getGraphics();

		System.gc();

		instance = this;
	}

	public void update(double deltaTime) {
		for (GameObject gameObject : gameObjects) {
			gameObject.update(deltaTime);
		}

		for (IUpdateable upject : updateable) {
			upject.update(deltaTime);
		}
	}

	public void updateNetwork(double deltaTime) {

	}

	public void render(Graphics g, ImageObserver observer) {
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, Screen.width, Screen.height);

		for (GameObject gameObject : gameObjects) {
			gameObject.draw(g2, observer);
		}

		for (IDrawable iDrawable : drawable) {
			iDrawable.draw(g, observer);
		}

		for (IGuiOverlay iGuiOverlay : guiOverlays) {
			iGuiOverlay.draw(g, observer);
		}

		if (Refrence.DEBUG_MODE) {
			g2.setColor(Color.GREEN);
			g2.setFont(new Font("Arial", 1, 20));
			g2.drawString(stats, 25, 60);
		}

		g.drawImage(this.i, 0, 0, this.frame);
	}

	private void run() {
		long time = System.nanoTime();
		final double tick = 60.0;
		double ms = 1000000000 / tick;
		double deltaTime = 0;
		int ticks = 0;
		int fps = 0;
		long timer = System.currentTimeMillis();
		long frameLimit = 80;
		long currentTime;
		int seconds = 0;
		int minutes = 0;
		int hours = 0;

		while (running) {
			currentTime = System.nanoTime();
			deltaTime += (currentTime - time) / ms;
			time = currentTime;

			if (deltaTime >= 1) {
				ticks++;
				update(deltaTime);
				deltaTime--;
			}

			synchronized (rlo) {
				rlo.setWasNotified(true);
				rlo.notify();
			}
			fps++;

			while (deltaTime < frameLimit) {
				currentTime = System.nanoTime();
				deltaTime += (currentTime - time) / ms;
				time = currentTime;
			}

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				seconds++;
				if (seconds > 60) {
					seconds %= 60;
					minutes++;

					if (minutes > 60) {
						minutes %= 60;
						hours++;
					}
				}

				// GT stands for GameTime.
				stats = "Ticks: " + ticks + ", FPS: " + fps + ", GT: " + ((hours < 10) ? "0" + hours : hours) + ":"
						+ ((minutes < 10) ? "0" + minutes : minutes) + ":" + ((seconds < 10) ? "0" + seconds : seconds);
				if (Refrence.DEBUG_MODE) {
					System.out.println(stats);
				}
				ticks = 0;
				fps = 0;
				if (Refrence.DEBUG_MODE) {
					System.out.println(Runtime.getRuntime().freeMemory());
				}
				System.gc();
				if (Refrence.DEBUG_MODE) {
					System.out.println(Runtime.getRuntime().freeMemory());
				}
			}
		}
	}

	public JFrame getFrame() {
		return this.frame;
	}

	public Graphics getG() {
		return this.g;
	}

	public Graphics getG2() {
		return this.g2;
	}

	public BufferedImage getI() {
		return this.i;
	}
}
/*
 * -XX:+UnlockCommercialFeatures -XX:+FlightRecorder
 * -XX:FlightRecorderOptions=stackdepth=2048
 * -XX:StartFlightRecording=duration=60m,filename=GameTemplate.jfr
 */