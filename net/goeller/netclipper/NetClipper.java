/*
 * Copyright by Christian Goeller
 *
 * $HeadURL$
 * $LastChangedDate$
 */
package net.goeller.netclipper;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import org.apache.xmlrpc.WebServer;
import org.apache.xmlrpc.XmlRpcClient;

/**
 * @author $LastChangedBy$
 * @version $LastChangedRevision$
 */
public class NetClipper
{
	/** The listeners */
	private Set<INetClipboardListener> netCliboardListeners;

	/** The Clipboard */
	private Clipboard systemClipboard;

	/** XML-RPC Stuff */
	private URL serverUrl;
	private XmlRpcClient xmlrpcClient;
	private WebServer xmlrpcServer;

	/** Config stuff */
	private final int LOCAL_PORT = Integer.parseInt(NetClipperApp.config
			.getProperty("local.port", "9999"));

	private final String REMOTE_ADDRESS = NetClipperApp.config
			.getProperty("remote.address");

	private final String HANDLER_NAME = "clipboard";
	
	/** Timer */
	private Timer timer;
	private final int UPDATE_TIME = 500;
	private final int FIRST_DELAY = 1000;

	/** */
	private String oldData;

	/**
	 * Creates a new NetClipper
	 */
	public NetClipper() {
		netCliboardListeners = new HashSet<INetClipboardListener>();
		systemClipboard = java.awt.Toolkit.getDefaultToolkit()
				.getSystemClipboard();

		try {
			serverUrl = new URL(REMOTE_ADDRESS);
			xmlrpcClient = new XmlRpcClient(serverUrl);

		} catch (MalformedURLException e) {
			System.err.println(e.getMessage());
		}

		xmlrpcServer = new WebServer(LOCAL_PORT);
		xmlrpcServer.addHandler(HANDLER_NAME, this);
		xmlrpcServer.start();

		timer = new Timer();

		timer.scheduleAtFixedRate(new TimerTask()
		{
			@Override
			public void run() {
				synchronized (NetClipper.this) {
					String newData = loadFromClipboard();
					if (newData != oldData) {
						sendToPeer(newData);
					}
				}
			}

		}, FIRST_DELAY, UPDATE_TIME);
	}

	/**
	 * Adds a INetClipboardListener
	 * 
	 * @param listener
	 */
	public void addNetClipboardListener(INetClipboardListener listener) {
		netCliboardListeners.add(listener);
	}

	/**
	 * Removes the INetClipboardListener
	 * 
	 * @param listener
	 */
	public void removeNetClipboardListener(INetClipboardListener listener) {
		netCliboardListeners.remove(listener);
	}

	/**
	 * Informs the listeners about recieved data
	 * 
	 * @param data
	 */
	public void informListeners(String data) {
		for (INetClipboardListener listener : netCliboardListeners) {
			listener.dataRecieved(data);
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
		synchronized (this) {
			oldData = data;
			storeInClipboard(data);
		}
		informListeners(data);
		return 0;
	}

	/**
	 * XML-RPC Method
	 * 
	 * @return the clipboard content
	 */
	public String get() {
		return loadFromClipboard();
	}

	/**
	 * Loads data from the clipboard
	 * 
	 * @return the string on the clipboard
	 */
	public String loadFromClipboard() {
		Transferable transfer = systemClipboard.getContents(null);

		try {
			if (transfer.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				String data = (String) transfer
						.getTransferData(DataFlavor.stringFlavor);
				return data;
			}
		} catch (Exception e1) {
			// ignore exceptions
		}
		return "";
	}

	/**
	 * Stores data from the pane in the clipboard
	 */
	public void storeInClipboard(String string) {
		StringSelection data = new StringSelection(string);
		systemClipboard.setContents(data, data);
	}

	/**
	 * Sends data to the peer
	 */
	public void sendToPeer(String data) {
		Vector<String> params = new Vector<String>();
		params.add(data);

		try {
			xmlrpcClient.execute("clipboard.set", params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets data from the peer
	 */
	public String fetchFromPeer() {
		Vector params = new Vector();

		try {
			String data = (String) xmlrpcClient
					.execute("clipboard.get", params);
			return data;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
