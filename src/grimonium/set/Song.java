package grimonium.set;

import microkontrol.MicroKontrol;
import processing.xml.XMLElement;

public class Song {

	private String name;
	private Scene stage;
	public ClipGroup[] groups;
	private int sceneOffset;

	public Song(XMLElement element) {
		name = element.getStringAttribute("name");
		stage = new Scene(element.getChild("stage"));
		sceneOffset = element.getIntAttribute("sceneoffset");

		XMLElement[] audios = element.getChildren("audio");
		XMLElement[] midis = element.getChildren("midi");
		groups = new ClipGroup[audios.length + midis.length];

		addGroups(concat(audios,midis));
	}

	private void addGroups(XMLElement[] children) {
		adjustSceneOffsets(children);
		for (int i = 0; i < children.length; i++) {
			XMLElement element = children[i];
			ClipGroup group = createGroup(element);
			groups[i] = group;
		}

	}

	private void adjustSceneOffsets(XMLElement[] groups) {
		for (int i = 0; i < groups.length; i++) {
			XMLElement group = groups[i];
			adjustSceneOffset(group.getChildren("pad"));
		}
	}

	private void adjustSceneOffset(XMLElement[] pads) {
		for (int i = 0; i < pads.length; i++) {
			XMLElement pad = pads[i];
			Integer newOffset = pad.getIntAttribute("scene") + sceneOffset;
			pad.setAttribute("scene", newOffset.toString());
		}
	}

	private ClipGroup createGroup(XMLElement element) {
		String name = element.getName();
		if (name.equals("audio")) return new AudioGroup(element);
		if (name.equals("midi")) return new MidiGroup(element);
		System.out.println("Error, only <audio> and <midi> elements are supported at the top level in a song - you tried "+ name);
		return null;
	}

	public void activate() {
		MicroKontrol.getInstance().lcds[8].setText(name);
		for (int i = 0; i < groups.length; i++) {
			ClipGroup group = groups[i];
			group.activate();
		}
	}
	public void deactivate(){
		for (int i = 0; i < groups.length; i++) {
			ClipGroup group = groups[i];
			group.deactivate();
		}
	}

	public static XMLElement[] concat(XMLElement[] A, XMLElement[] B) {
		XMLElement[] C = new XMLElement[A.length + B.length];
		System.arraycopy(A, 0, C, 0, A.length);
		System.arraycopy(B, 0, C, A.length, B.length);
		return C;
	}
	@Override
	public String toString() {
		return super.toString() + "[" + name + "]";
	}

}
