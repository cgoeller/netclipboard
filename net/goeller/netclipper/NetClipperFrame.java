/*
 * Copyright by Christian Goeller
 *
 * $HeadURL$
 * $LastChangedDate$
 */
package net.goeller.netclipper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author $LastChangedBy$
 * @version $LastChangedRevision$
 */
public class NetClipperFrame extends JFrame implements INetClipboardListener
{
	/** The default serial version id */
	private static final long serialVersionUID = 1L;

	/** The netclipper */
	private NetClipper netClipper;

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

	/**
	 * Creates a new NetClipperFrame
	 */
	public NetClipperFrame(NetClipper netClipper) {
		super("NetClipper - The Network Clipboard");

		this.netClipper = netClipper;
		netClipper.addNetClipboardListener(this);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		initializeComponents();
		initializeListeners();
		build();

		setString(netClipper.loadFromClipboard());
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
	}

	/**
	 * Initializes listeners
	 */
	private void initializeListeners() {

		btRefresh.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				setString(netClipper.loadFromClipboard());
			}
		});

		btStore.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				netClipper.storeInClipboard(getString());
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
				netClipper.sendToPeer(getString());
			}
		});

		btFetch.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				setString(netClipper.fetchFromPeer());
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
		JScrollPane scrollPane = new JScrollPane(tpData);
		builder.add(scrollPane, cc.xyw(2, 3, 4));

		builder.add(btExit, cc.xy(2, 5, "left,center"));
		builder.add(btRefresh, cc.xy(3, 5));
		builder.add(btStore, cc.xy(5, 5));
		builder.add(btSend, cc.xy(3, 7));
		builder.add(btFetch, cc.xy(5, 7));

		container.add(builder.getPanel(), BorderLayout.CENTER);
	}

	/**
	 * @see net.goeller.netclipper.INetClipboardListener#dataRecieved(java.lang.String)
	 */
	public void dataRecieved(String data) {
		setString(data);
	}

	/**
	 * Returns the text of the text pane
	 * 
	 * @return the text of the text pane
	 */
	private String getString() {
		return tpData.getText();
	}

	/**
	 * Sets text to the text pane
	 * 
	 * @param text
	 *            the text to set
	 */
	private void setString(String text) {
		tpData.setText(text);
	}
}
