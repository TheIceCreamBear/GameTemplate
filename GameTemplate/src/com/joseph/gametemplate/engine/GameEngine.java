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
import com.joseph.gametemplate.reference.Reference;
import com.joseph.gametemplate.screen.Screen;
import com.joseph.gametemplate.threads.RenderThread;
import com.joseph.gametemplate.threads.ShutdownThread;

/**
 * @author David Santamaria - Original Author
 * @author Joseph Terribile - Current Maintainer
 */
public class GameEngine {

	/**
	 * boolean that expressed the state of the engine, whether it is
	 * <code> running </code> or not
	 */
	private static boolean running = true;
	private static Random rand;
	/**
	 * The instance of the GameEngine
	 */
	private static GameEngine instance;
	/**
	 * Displayed at the top of the screen. Expresses the fps, and time and other
	 * such things
	 */
	private static String stats = "";

	/**
	 * Used to display the screen
	 */
	private JFrame frame;

	/**
	 * First graphics instance
	 */
	private Graphics g;
	/**
	 * BufferedImage graphics instance
	 */
	private Graphics g2;
	/**
	 * Image that is displayed on the screen
	 */
	private BufferedImage i;

	// Threads
	private RenderLockObject rlo;
	private RenderThread rtInstance;
	private ShutdownThread sdtInstance;

	/**
	 * ArrayList of GameObjects - to be looped over to update and draw
	 */
	private static ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
	/**
	 * Only updatable objects, looped through to update
	 */
	private static ArrayList<IUpdateable> updateable = new ArrayList<IUpdateable>();
	/**
	 * Drawable only objects
	 */
	private static ArrayList<IDrawable> drawable = new ArrayList<IDrawable>();
	private static ArrayList<IGuiOverlay> guiOverlays = new ArrayList<IGuiOverlay>();

	private static boolean[] isKeyPressed = new boolean[256];

	public static boolean[] getKeyPressedArray() {
		return isKeyPressed;
	}

	/**
	 * 
	 * @return the instance of the GameEngine
	 */
	public static GameEngine getInstance() {
		return instance;
	}

	/**
	 * I don't even know why this is a thing
	 * 
	 * @return instance of a random
	 */
	public static Random getRand() {
		return rand;
	}

	/**
	 * 
	 * @return true if <code> running </code>, false otherwise
	 */
	public static boolean isRunning() {
		return running;
	}

	public static void main(String[] args) {
		if (Reference.DEBUG_MODE) {
			System.out.println(Runtime.getRuntime().maxMemory());
			System.err.println("x: " + Screen.width + "y: " + Screen.height);
		}
		instance = new GameEngine();
		instance.run();
	}

	/**
	 * Starts the GameEngine
	 */
	public static void startGameEngine() {
		instance = new GameEngine();
		instance.run();
	}

	/**
	 * Initializes and instantiates
	 */
	public GameEngine() {
		initialize();
	}

	/**
	 * 
	 * @return The keylistener used for the frame
	 */
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
				// NOOP
			}

		};
	}

	/**
	 * Why is this a thing? @Joseph
	 */
	public void reinitialize() {

	}

	/**
	 * Initializes all the stuff
	 */
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

	/**
	 * Loops through all the updatables and updates them
	 * 
	 * @param deltaTime
	 *            - Time between each frame (used to evaluate things within
	 *            update methods of each object)
	 */
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

	/**
	 * Loops through all the Drawables and draws them
	 * 
	 * @param g
	 *            Graphics instance to draw upon
	 * @param observer
	 *            observer to put graphics instance upon
	 */
	private void render(Graphics g, ImageObserver observer) {
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

		if (Reference.DEBUG_MODE) {
			g2.setColor(Color.GREEN);
			g2.setFont(Reference.DEFAULT_FONT);
			g2.drawString(stats, 25, 60);
		}

		g.drawImage(this.i, 0, 0, this.frame);
	}

	/**
	 * The render method with global visibility
	 */
	public void render() {
		render(g, frame);
	}

	/**
	 * Runs the GameEngine
	 */
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
				if (Reference.DEBUG_MODE) {
					System.out.println(stats);
				}
				ticks = 0;
				fps = 0;
				if (Reference.DEBUG_MODE) {
					System.out.println(Runtime.getRuntime().freeMemory());
				}
				System.gc();
				if (Reference.DEBUG_MODE) {
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