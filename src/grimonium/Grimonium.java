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

// import ddf.minim.AudioInput;
// import ddf.minim.Minim;
import java.awt.Component;

import grimonium.gui.Animator;
import grimonium.set.GrimoniumSet;
import microkontrol.MicroKontrol;
import microkontrol.controls.Joystick;
import netP5.NetAddress;
import netP5.NetInfo;
import oscP5.OscP5;
import processing.core.PApplet;
import processing.xml.XMLElement;
import rwmidi.RWMidi;

/**
 * @author Michael Forrest gonna use midi channel 4 for controller messages
 */
public class Grimonium {

	PApplet applet;

	public final String VERSION = "0.1.0";

	private OscP5 oscP5;

	public GrimoniumSet set;

	private Joystick joystick;

	// private Minim minim;

	// private AudioInput in;

	public Grimonium(PApplet applet, String config_xml, String mapping_xml) {
		this.applet = applet;
		XMLElement xml = new XMLElement(applet, config_xml);
		if (xml == null)
			PApplet.println("Please supply a config xml file path.");

		boot(applet, xml);

		XMLElement mapping = new XMLElement(applet, mapping_xml);
		loadSet(mapping);
		joystick = MicroKontrol.getInstance().joystick;
		applet.registerDraw(this);

	}
	public void draw()	{
		if(joystick.getX() != 0) Animata.panLayer(joystick.getX() * 20f);
		if(joystick.getY() != 0)Animata.zoomCamera( ((float)joystick.getY() * 20.0f));
	}
	private void loadSet(XMLElement child) {
		PApplet.println("name:" + child.getName());
		set = new GrimoniumSet(child);


	}

	private void boot(PApplet applet, XMLElement xml) {
		oscP5 = new OscP5(applet, 12000);
		MicroKontrol.init(applet);

		NetAddress netAddress = new NetAddress(NetInfo.getHostAddress(), 7110);
		PApplet.println("Sending Animata stuff to " + netAddress.address());
		Animata.init(netAddress, oscP5);
		Animator.init(applet);
		// minim = new Minim(applet);
		// in = minim.getLineIn(Minim.STEREO, 512);
		if (xml.getChild("ableton") != null)
			Ableton.init(xml.getChild("ableton"),applet);
		if (xml.getChild("live_api") != null)
			LiveAPI.init(xml.getChild("live_api"));
	}

	public String version() {
		return VERSION;
	}



}
