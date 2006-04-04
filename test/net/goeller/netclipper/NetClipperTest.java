/*
 * Copyright by Christian Goeller
 *
 * $HeadURL:svn+ssh://z990/home/chris/Daten/Repositories/netclipboard/netclipboard/source/net/goeller/netclipper/INetClipboardListener.java $
 * $LastChangedDate:2006-04-04 21:37:29 +0200 (Di, 04 Apr 2006) $
 */
package net.goeller.netclipper;

import static org.easymock.EasyMock.*;
import junit.framework.TestCase;


/**
 * @author $LastChangedBy:chris $
 * @version $LastChangedRevision:26 $
 */
public class NetClipperTest extends TestCase
{

	private NetClipper netClipper;
	private INetClipboardListener listener;
	
	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		
		netClipper = new NetClipper();
		listener = createMock(INetClipboardListener.class);
	}

	/*
	 * Test method for 'net.goeller.netclipper.NetClipper.addNetClipboardListener(INetClipboardListener)'
	 */
	public void testAddNetClipboardListener()
	{
		listener.dataRecieved("TEST");
		expectLastCall().once();
		replay(listener);
		
		netClipper.addNetClipboardListener(listener);
		netClipper.set("TEST");
		verify(listener);
		
	}

	/*
	 * Test method for 'net.goeller.netclipper.NetClipper.removeNetClipboardListener(INetClipboardListener)'
	 */
	public void testRemoveNetClipboardListener()
	{
		netClipper.addNetClipboardListener(listener);
		netClipper.removeNetClipboardListener(listener);
	}

	/*
	 * Test method for 'net.goeller.netclipper.NetClipper.informListeners(String)'
	 */
	public void testInformListeners()
	{

	}
}
