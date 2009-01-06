package grimonium;

import microkontrol.MicroKontrol;
import microkontrol.controls.LCD;
import processing.xml.XMLElement;


public class GroupElement{

	protected boolean active = false;
	public LCDHint[] lcds;

	public void activate() {
		active = true;
	}

	public void deactivate() {
		active = false;
	}

	protected void addLCDs(XMLElement[] children, String colour) {
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
			text = element.getStringAttribute("text");
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

}
