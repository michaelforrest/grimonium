package grimonium.maps;


import grimonium.Ableton;
import grimonium.LiveAPI;

import java.util.ArrayList;

import microkontrol.MicroKontrol;
import microkontrol.controls.LCD;
import processing.xml.XMLElement;


public class MapBase  {
	public class ClipMap {
		private int track = Ableton.getVisualsTrack();
		private int scene;
		public ClipMap(XMLElement element) {
			scene = element.getIntAttribute("scene") + element.getParent().getParent().getParent().getIntAttribute("sceneoffset");
		}
		public void stop() {
			Ableton.stopTrack(track);
		}
		public void play() {
			System.out.println("triggering clip map " + track + " , " + scene);
			LiveAPI.trigger(track, scene);
		}

	}
	public class Message {
		public final int channel;
		public Message(int channel) {
			this.channel = channel-1;
		}
	}
	public class NoteMessage extends Message{
		public final Integer note;
		public int velocity = 64;
		NoteMessage(int channel,int note){
			super(channel);
			this.note = note;
		}
		NoteMessage(int channel,int note, int velocity){
			super(channel);
			this.note = note;
			this.velocity = velocity;
		}

	}
	public class CC extends Message {
		public final int cc;
		public CC(int channel, int cc) {
			super(channel);
			this.cc = cc;
		}
		public CC(XMLElement xml) {
			super(xml.getIntAttribute("channel"));
			cc = xml.getIntAttribute("cc");
		}

		public void send(int value) {
			Ableton.sendCC(channel, cc, value);
		}

	}

	protected boolean active = false;
	public LCDHint[] lcds;

	public void activate() {
		active = true;
	}

	public void deactivate() {
		active = false;
	}

	protected void addLCDs(XMLElement child, String colour) {
		XMLElement[] children = child.getChildren("lcd");
		lcds = new LCDHint[children.length];
		for (int i = 0; i < children.length; i++) {
			XMLElement element = children[i];
			lcds[i]=  new LCDHint(element, colour);
		}

	}
	public class LCDHint{

		private int id;
		private String text;
		private LCD lcd;
		public String colour;

		public LCDHint(XMLElement element, String colour) {
			this.colour = element.getStringAttribute("colour", colour);
			id = element.getIntAttribute("id");
			text = element.getStringAttribute("text", element.getParent().getStringAttribute("name"));
			lcd = MicroKontrol.getInstance().lcds[id];
		}

		public void revert() {
			lcd.set("", LCD.GREEN);
		}

		public void activateHint() {
			lcd.set(text,colour);
		}

	}
	protected void turnOffLCDHints() {
		for (int i = 0; i < lcds.length; i++) {
			LCDHint lcd = lcds[i];
			lcd.revert();
		}
	}

	protected void turnOnLCDHints() {

		for (int i = 0; i < lcds.length; i++) {
			LCDHint lcd = lcds[i];
			lcd.activateHint();
		}

	}

	public static ArrayList<EncoderMap> collectEncoders(MapBase[] groupElements) {
		ArrayList<EncoderMap> result = new ArrayList<EncoderMap>();
		if(groupElements == null) return result;
		for (MapBase element : groupElements) {
			if(element instanceof EncoderMap) result.add((EncoderMap)element);
		}
		return result;
	}

	public static ArrayList<FaderMap> collectFaders(MapBase[] groupElements) {
		ArrayList<FaderMap> result = new ArrayList<FaderMap>();
		if(groupElements == null) return result;
		for (MapBase element : groupElements) {
			if(element instanceof FaderMap) result.add((FaderMap)element);
		}
		return result;
	}

	public static KeyboardMap findKeyboard(MapBase[] controls) {
		for (MapBase element : controls) {
			if(element instanceof KeyboardMap) return (KeyboardMap) element;
		}
		return null;
	}

}
