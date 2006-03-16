/*
 * Copyright 2006 by Christian Göller 
 *
 */
package net.goeller.netclipper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JTextPane;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Christian Göller
 * @version 16.03.2006
 * 
 */
public class NetClipperFrame extends JFrame
{
	/** The default serial version id */
	private static final long serialVersionUID = 1L;

	/** The layout */
	private final static String colLayout = "10dlu,fill:pref:grow,10dlu";
	private final static String rowLayout = "10dlu,pref,fill:pref:grow,10dlu";

	/** Components */
	JTextPane tpData;

	/**
	 * Creates a new NetClipperFrame
	 */
	public NetClipperFrame() {
		super("NetClipper - The Network Clipboard");

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		initializeComponents();
		build();
	}

	/**
	 * Initializes components
	 */
	private void initializeComponents() {
		tpData = new JTextPane();
		tpData.setPreferredSize(new Dimension(200, 10));
		tpData.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

	/**
	 * Builds the panel
	 */
	private void build() {

		Container container = getContentPane();
		container.setLayout(new BorderLayout());

		FormLayout layout = new FormLayout(colLayout, rowLayout);
		PanelBuilder builder = new PanelBuilder(layout);
		CellConstraints cc = new CellConstraints();

		builder.addSeparator("Clipboard Contents:", cc.xy(2, 2));
		builder.add(tpData, cc.xy(2, 3));

		container.add(builder.getPanel(), BorderLayout.CENTER);
	}
}
