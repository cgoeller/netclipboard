/*
 * Copyright 2006 by Christian Göller 
 *
 */
package net.goeller.netclipper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
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
	private final static String colLayout = "10dlu,fill:pref:grow,pref,5dlu,pref,10dlu";
	private final static String rowLayout = "10dlu,pref,fill:min:grow,5dlu,pref,10dlu";

	/** Components */
	private JTextPane tpData;
	private JButton btRefresh;
	private JButton btStore;
	private JButton btExit;

	/** The Clipboard */
	private Clipboard systemClipboard;

	/**
	 * Creates a new NetClipperFrame
	 */
	public NetClipperFrame() {
		super("NetClipper - The Network Clipboard");

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		initializeComponents();
		initializeListeners();
		build();

		refresh();
	}

	/**
	 * Initializes components
	 */
	private void initializeComponents() {
		tpData = new JTextPane();
		tpData.setPreferredSize(new Dimension(300, 10));
		tpData.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		btRefresh = new JButton("Refresh");
		btRefresh.setMnemonic('R');

		btStore = new JButton("Store");
		btStore.setMnemonic('S');

		btExit = new JButton("Exit");
		btExit.setMnemonic('X');

		systemClipboard = java.awt.Toolkit.getDefaultToolkit()
				.getSystemClipboard();
	}

	/**
	 * Initializes listeners
	 */
	private void initializeListeners() {

		btRefresh.addActionListener(new ActionListener()
		{
			/**
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
				refresh();
			}
		});

		btStore.addActionListener(new ActionListener()
		{
			/**
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
				store();
			}
		});

		btExit.addActionListener(new ActionListener()
		{
			/**
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}

	/**
	 * Builds the panel
	 */
	private void build() {

		Container container = getContentPane();
		container.setLayout(new BorderLayout());

		FormLayout layout = new FormLayout(colLayout, rowLayout);
		layout.setColumnGroups(new int[][] { { 3, 5 } });
		PanelBuilder builder = new PanelBuilder(layout);
		CellConstraints cc = new CellConstraints();

		builder.addSeparator("Clipboard Contents:", cc.xyw(2, 2, 4));
		builder.add(tpData, cc.xyw(2, 3, 4));

		builder.add(btExit, cc.xy(2, 5, "left,center"));
		builder.add(btRefresh, cc.xy(3, 5));
		builder.add(btStore, cc.xy(5, 5));

		JScrollPane scrollPane = new JScrollPane(builder.getPanel());

		container.add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * Loads data from the clipboard
	 */
	private void refresh() {
		Transferable transfer = systemClipboard.getContents(null);

		try {
			String data = (String) transfer
					.getTransferData(DataFlavor.stringFlavor);
			tpData.setText(data);
		} catch (UnsupportedFlavorException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Stores data from the pane in the clipboard
	 */
	private void store() {

		StringSelection data = new StringSelection(tpData.getText());
		systemClipboard.setContents(data, data);
	}
}
