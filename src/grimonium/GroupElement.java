package grimonium;

import grimonium.set.CCEncoder;
import grimonium.set.GroupFader;

import java.util.ArrayList;

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

	public static ArrayList<CCEncoder> collectEncoders(GroupElement[] groupElements) {
		ArrayList<CCEncoder> result = new ArrayList<CCEncoder>();
		if(groupElements == null) return result;
		for (GroupElement element : groupElements) {
			if(element instanceof CCEncoder) result.add((CCEncoder)element);
		}
		return result;
	}

	public static ArrayList<GroupFader> collectFaders(GroupElement[] groupElements) {
		ArrayList<GroupFader> result = new ArrayList<GroupFader>();
		if(groupElements == null) return result;
		for (GroupElement element : groupElements) {
			if(element instanceof GroupFader) result.add((GroupFader)element);
		}
		return result;
	}

}
