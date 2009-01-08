package grimonium.maps;


import java.util.ArrayList;
import java.util.Observable;

import microkontrol.MicroKontrol;
import microkontrol.controls.LCD;
import processing.xml.XMLElement;


public class ControlMap  {

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
			this.colour = colour;
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

	public static ArrayList<EncoderMap> collectEncoders(ControlMap[] groupElements) {
		ArrayList<EncoderMap> result = new ArrayList<EncoderMap>();
		if(groupElements == null) return result;
		for (ControlMap element : groupElements) {
			if(element instanceof EncoderMap) result.add((EncoderMap)element);
		}
		return result;
	}

	public static ArrayList<FaderMap> collectFaders(ControlMap[] groupElements) {
		ArrayList<FaderMap> result = new ArrayList<FaderMap>();
		if(groupElements == null) return result;
		for (ControlMap element : groupElements) {
			if(element instanceof FaderMap) result.add((FaderMap)element);
		}
		return result;
	}

	public static KeyboardMap findKeyboard(ControlMap[] controls) {
		for (ControlMap element : controls) {
			if(element instanceof KeyboardMap) return (KeyboardMap) element;
		}
		return null;
	}

}
