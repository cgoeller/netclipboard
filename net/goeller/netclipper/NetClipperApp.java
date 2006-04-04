/*
 * Copyright by Christian Goeller
 *
 * $HeadURL$
 * $LastChangedDate$
 */ 
package net.goeller.netclipper;

import java.awt.Dimension;
import java.io.FileInputStream;
import java.util.Properties;

import javax.swing.UIManager;

/**
 * @author $LastChangedBy$
 * @version $LastChangedRevision$
 */
public class NetClipperApp
{

	public static Properties config;

	/**
	 * Starts the application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			config = new Properties();
			config.load(new FileInputStream("config.properties"));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		loadLookAndFeel();
		NetClipperFrame frame = new NetClipperFrame();
		frame.setSize(new Dimension(400, 300));
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
			System.err.println(ex.getMessage());
		}

	}

}
