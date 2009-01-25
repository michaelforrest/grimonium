/*
  Michael Forrest's live performance framework for Processing

  (c) copyright Michael Forrest

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General
  Public License along with this library; if not, write to the
  Free Software Foundation, Inc., 59 Temple Place, Suite 330,
  Boston, MA  02111-1307  USA
 */

package grimonium;

import grimonium.gui.Animator;
import grimonium.set.GrimoniumSet;
import microkontrol.MicroKontrol;
import netP5.NetAddress;
import netP5.NetInfo;
import oscP5.OscP5;
import processing.core.PApplet;
import processing.xml.XMLElement;

/**
 *
 */
public class Grimonium {

	PApplet applet;

	private static OscP5 oscP5;
	public GrimoniumSet set;

	public Grimonium(PApplet applet, String config_xml, String mapping_xml) {
		this.applet = applet;
		new GrimoniumOutput(7111,applet);
		XMLElement xml = new XMLElement(applet, config_xml);
		boot(applet, xml);
		XMLElement mapping = new XMLElement(applet, mapping_xml);
		set = new GrimoniumSet(mapping);


	}

	private static void boot(PApplet applet, XMLElement xml) {
		boot(applet);
		Ableton.init(xml.getChild("ableton"), applet);
		LiveAPI.init(xml.getChild("live_api"));
	}

	public static void boot(PApplet applet) {
		oscP5 = new OscP5(applet, 12000);
		MicroKontrol.init(applet);
		Animator.init(applet);
	}

	public static void startAnimataOSC() {
		NetAddress netAddress = new NetAddress(NetInfo.getHostAddress(), 7110);
		PApplet.println("Sending Animata stuff to " + netAddress.address());
		Animata.init(netAddress, oscP5);
	}


}
