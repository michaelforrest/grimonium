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

import ddf.minim.AudioInput;
import ddf.minim.Minim;
import processing.core.PApplet;
import processing.xml.XMLElement;
import oscP5.*;
import microkontrol.*;
import netP5.*;
/**
 * @author Michael Forrest
 */
public class Grimonium {

	PApplet applet;

	public final String VERSION = "0.1.0";

	private LiveAPI liveAPI;

	private OscP5 oscP5;

	private MicroKontrol mk;

	private NetAddress animata;

	private Minim minim;

	private AudioInput in;

	private Ableton ableton;

	public Grimonium(PApplet applet, String config_xml, String mapping_xml) {
		this.applet = applet;
		XMLElement xml = new XMLElement(applet, config_xml);
		if (xml == null)
			PApplet.println("Please supply a config xml file path.");

		boot(applet, xml);

		XMLElement mapping = new XMLElement(applet,mapping_xml);
		addChromaticTracks(mapping);
	}

	private void addChromaticTracks(XMLElement xml) {
		XMLElement[] chromaticTracks = xml.getChildren("chromatic_track");
		for(int i=0; i<chromaticTracks.length; i++){
			XMLElement trackXml = chromaticTracks[i];
			new ChromaticTrack(trackXml, mk, liveAPI);
		}

	}

	private void boot(PApplet applet, XMLElement xml) {
		oscP5 = new OscP5(applet, 12000);
		mk = new MicroKontrol(applet);

		animata = new NetAddress(NetInfo.getHostAddress(), 7110);

		minim = new Minim(applet);
		in = minim.getLineIn(Minim.STEREO, 512);
		ableton = new Ableton(mk);
		liveAPI = new LiveAPI(xml.getChild("live_api"));
	}

	public String version() {
		return VERSION;
	}

}
