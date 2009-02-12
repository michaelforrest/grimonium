package grimonium.set;

import grimonium.gui.Colours;
import grimonium.maps.ElementFactory;
import grimonium.maps.MapBase;
import processing.xml.XMLElement;

public class ClipGroup {



	private int track;
	private MapBase[] elements;
	public Clip[] pads;
	private String id;
	public int colour;

	public ClipGroup(XMLElement element) {
		id = element.getStringAttribute("id");
		track = element.getIntAttribute("track");
		colour = Colours.get(element.getStringAttribute("colour"));
		addElements(element.getChildren());
		createPadsArray();
		validatePadsArray();
	}

	public ClipGroup(){

	}

	private void validatePadsArray() {
		for (int i = 0; i < pads.length; i++) {
			Clip pad = pads[i];
			checkForDuplicates(pad);
		}
	}

	private void checkForDuplicates(Clip pad) {
		for (int i = 0; i < pads.length; i++) {
			Clip checked = pads[i];
			if(checked!=pad){
				if(pad.track == checked.track && pad.scene == checked.scene) System.out.println("ERROR, you have duplicated a clip in your XML - in group "+ id +"," + pad.pad_id +" is a duplicate");
				//if(pad.id == checked.id) System.out.println("Error, you can't assign two clips to the same pad");
			}
		}

	}

	private void createPadsArray() {
		int length = countPads();
		pads = new Clip[length];
		int index = 0;
		for (int i = 0; i < elements.length; i++) {
			MapBase element = elements[i];
			if(element instanceof Clip ){
				pads[index] = (Clip) element;
				index ++;
			}
		}
	}

	private int countPads() {
		int length = 0;
		for (int i = 0; i < elements.length; i++) {
			MapBase element = elements[i];
			if (element instanceof Clip) length ++;
		}
		return length;
	}

	private void addElements(XMLElement[] children) {
		elements = new MapBase[children.length];
		for (int i = 0; i < children.length; i++) {
			XMLElement element = children[i];
			element.setAttribute("track", Integer.toString(track));

			elements[i] = createElement(element);
		}

	}

	private MapBase createElement(XMLElement element) {
		String name = element.getName();
		if(name.equals("clip")) return new Clip(element, this);
		return ElementFactory.create(element);
	}

	public void activate() {
		for (int i = 0; i < elements.length; i++) {
			MapBase element = elements[i];
			element.activate();
		}
	}

	public void deactivate() {
		for (int i = 0; i < elements.length; i++) {
			MapBase element = elements[i];
			element.deactivate();
		}

	}

	public Clip[] getPads() {
		return pads;
	}

	public void clearAllPlaying() {
		for (int i = 0; i < pads.length; i++) {
			Clip pad = pads[i];
			pad.clearPlaying();
		}

	}


}
