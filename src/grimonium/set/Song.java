package grimonium.set;

import microkontrol.MicroKontrol;
import processing.xml.XMLElement;

public class Song {

	private String name;
	private Scene scene;
	private ClipGroup[] groups;

	public Song(XMLElement element) {
		name = element.getStringAttribute("name");
		scene = new Scene(element.getChild("scene"));

		XMLElement[] audios = element.getChildren("audio");
		XMLElement[] midis = element.getChildren("midi");
		groups = new ClipGroup[audios.length + midis.length];

		addGroups(concat(audios,midis));
	}

	private void addGroups(XMLElement[] children) {
		for (int i = 0; i < children.length; i++) {
			XMLElement element = children[i];
			ClipGroup group = createGroup(element);
			groups[i] = group;
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

}
