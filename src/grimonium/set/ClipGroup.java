package grimonium.set;

import java.util.Hashtable;

import grimonium.FreqBone;
import grimonium.GroupElement;
import grimonium.NoteBone;
import processing.xml.XMLElement;

public class ClipGroup {
	public static Hashtable<String, Integer> COLOURS = new Hashtable<String, Integer>();
	static {
		COLOURS.put("beats", 0xf01229); // red
		COLOURS.put("rhythm", 0xffdc06); // yellow
		COLOURS.put("lead", 0xd707a5); // pink
		COLOURS.put("bass", 0x9858e0); //purple
		COLOURS.put("bits", 0x3c6a08); // green
		COLOURS.put("neutral", 0x8a6636); //brown
	}
	private int track;
	private GroupElement[] elements;
	SongPad[] pads;
	private String id;
	public int colour;

	public ClipGroup(XMLElement element) {
		id = element.getStringAttribute("id");
		track = element.getIntAttribute("track");
		try{
			colour = COLOURS.get(id);
		}catch(Exception e){
			System.out.println("ERROR, for now you need to choose an 'id' from the set: beats,rhythm,lead,bass,bits");
			colour = COLOURS.get("neutral");
		}
		addElements(element.getChildren());
		createPadsArray();
		validatePadsArray();
	}

	private void validatePadsArray() {
		for (int i = 0; i < pads.length; i++) {
			SongPad pad = pads[i];
			checkForDuplicates(pad);
		}
	}

	private void checkForDuplicates(SongPad pad) {
		for (int i = 0; i < pads.length; i++) {
			SongPad checked = pads[i];
			if(checked!=pad){
				if(pad.track == checked.track && pad.scene == checked.scene) System.out.println("ERROR, you have duplicated a clip in your XML - in group "+ id +"," + pad.pad_id +" is a duplicate");
				//if(pad.id == checked.id) System.out.println("Error, you can't assign two clips to the same pad");
			}
		}

	}

	private void createPadsArray() {
		int length = countPads();
		pads = new SongPad[length];
		int index = 0;
		for (int i = 0; i < elements.length; i++) {
			GroupElement element = elements[i];
			if(element instanceof SongPad ){
				pads[index] = (SongPad) element;
				index ++;
			}
		}
	}

	private int countPads() {
		int length = 0;
		for (int i = 0; i < elements.length; i++) {
			GroupElement element = elements[i];
			if (element instanceof SongPad) length ++;
		}
		return length;
	}

	private void addElements(XMLElement[] children) {
		elements = new GroupElement[children.length];
		for (int i = 0; i < children.length; i++) {
			XMLElement element = children[i];
			element.setAttribute("track", Integer.toString(track));

			elements[i] = createElement(element);
		}

	}

	private GroupElement createElement(XMLElement element) {
		String name = element.getName();
		if(name.equals("clip")) return new SongPad(element, this);
		return ElementFactory.create(element);
	}

	public void activate() {
		for (int i = 0; i < elements.length; i++) {
			GroupElement element = elements[i];
			element.activate();
		}
	}

	public void deactivate() {
		for (int i = 0; i < elements.length; i++) {
			GroupElement element = elements[i];
			element.deactivate();
		}

	}

	public SongPad[] getPads() {
		return pads;
	}

	public void clearAllPlaying() {
		for (int i = 0; i < pads.length; i++) {
			SongPad pad = pads[i];
			pad.clearPlaying();
		}

	}


}
