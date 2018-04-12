package com.joseph.gametemplate.gui;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JButton;

/**
 * Extension of JButton that makes all paint functions <code>NO-OP</code>, and implements {@link IGuiOverlay IGuiOverlay}
 * to allow for painting of the button using the methods available in the engine.
 * @author Joseph
 * @see IGuiOverlay
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractButton extends JButton implements IGuiOverlay {

	@Override
	protected void paintBorder(Graphics arg0) {
	}
	
	@Override
	public void paint(Graphics arg0) {
	}
	
	@Override
	protected void paintChildren(Graphics arg0) {
	}
	
	@Override
	protected void paintComponent(Graphics arg0) {
	}
	
	@Override
	public void paintImmediately(int arg0, int arg1, int arg2, int arg3) {
	}
	
	@Override
	public void paintImmediately(Rectangle arg0) {
	}
	
	@Override
	public void paintComponents(Graphics arg0) {
	}
	
	@Override
	public void paintAll(Graphics arg0) {
	}
	
}