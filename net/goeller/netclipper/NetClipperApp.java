/*
 * Copyright 2006 by Christian Göller 
 *
 */
package net.goeller.netclipper;

import java.awt.Dimension;

import javax.swing.UIManager;

/**
 * @author Christian Göller
 * @version 16.03.2006
 */
public class NetClipperApp
{

	/**
	 * Starts the application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		loadLookAndFeel();
		NetClipperFrame frame = new NetClipperFrame();
		frame.setSize(new Dimension(300, 200));
		frame.setVisible(true);
	}

	/**
	 * Sets the look and feel
	 */
	private static void loadLookAndFeel() {
		try {
			UIManager
					.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
		} catch (Exception ex) {
			System.out.println("Could not set Look&Feel");
		}

	}

}
