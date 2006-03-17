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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import org.apache.xmlrpc.WebServer;
import org.apache.xmlrpc.XmlRpcClient;

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
	private final static String rowLayout = "10dlu,pref,fill:min:grow,5dlu,pref,5dlu,pref,10dlu";

	/** Components */
	private JTextPane tpData;
	private JButton btRefresh;
	private JButton btStore;
	private JButton btExit;
	private JButton btSend;
	private JButton btFetch;

	/** The Clipboard */
	private Clipboard systemClipboard;

	/** XML-RPC Stuff */
	private URL serverUrl;
	private XmlRpcClient xmlrpcClient;
	private WebServer xmlrpcServer;

	/** Config stuff */

	private final int localPort = Integer.parseInt(NetClipperApp.config
			.getProperty("local.port", "9999"));

	private final String remoteAddress = NetClipperApp.config
			.getProperty("remote.address");

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

		btSend = new JButton("Send");
		btSend.setMnemonic('E');

		btFetch = new JButton("Fetch");
		btFetch.setMnemonic('F');

		systemClipboard = java.awt.Toolkit.getDefaultToolkit()
				.getSystemClipboard();

		try {

			serverUrl = new URL(remoteAddress);
			xmlrpcClient = new XmlRpcClient(serverUrl);

		} catch (MalformedURLException e) {
			System.err.println(e.getMessage());
		}

		xmlrpcServer = new WebServer(localPort);
		xmlrpcServer.addHandler("clipboard", this);
		xmlrpcServer.start();
	}

	/**
	 * Initializes listeners
	 */
	private void initializeListeners() {

		btRefresh.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				refresh();
			}
		});

		btStore.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				store();
			}
		});

		btExit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		btSend.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				send();
			}
		});

		btFetch.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				fetch();
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
		builder.add(btSend, cc.xy(3, 7));
		builder.add(btFetch, cc.xy(5, 7));

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

	/**
	 * Sends data to a client
	 */
	private void send() {
		Vector<String> params = new Vector<String>();
		params.add(tpData.getText());

		try {
			xmlrpcClient.execute("clipboard.set", params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets data from a client
	 */
	private void fetch() {
		Vector params = new Vector();

		try {
			String data = (String) xmlrpcClient
					.execute("clipboard.get", params);
			tpData.setText(data);
			store();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * XML-RPC Method
	 * 
	 * @param data
	 *            the data to put in the clipboard
	 * @return 0
	 */
	public int set(String data) {
		tpData.setText(data);
		store();
		return 0;
	}

	/**
	 * XML-RPC Method
	 * 
	 * @return the clipboard content
	 */
	public String get() {
		return tpData.getText();
	}
}
