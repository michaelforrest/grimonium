package grimonium.set;

import java.util.ArrayList;
import java.util.Observable;

import microkontrol.MicroKontrol;
import microkontrol.controls.LCD;
import processing.xml.XMLElement;

public class Song extends Observable{

	public static final String ACTIVATED = "activated";
	public static final String DEACTIVATED = "deactivated";
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
		LCD mainLCD = MicroKontrol.getInstance().lcds[8];
		mainLCD.setText(name);
		mainLCD.setColor("orange");
		for (int i = 0; i < groups.length; i++) {
			ClipGroup group = groups[i];
			group.activate();
		}
		setChanged();
		notifyObservers(ACTIVATED);
	}
	public void deactivate(){
		for (int i = 0; i < groups.length; i++) {
			ClipGroup group = groups[i];
			group.deactivate();
		}
		setChanged();
		notifyObservers(DEACTIVATED);
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

	public ArrayList<SongPad> getSongPads() {
		ArrayList<SongPad> result = new ArrayList<SongPad>();
		for (int i = 0; i < groups.length; i++) {
			ClipGroup group = groups[i];
			 addPadsTo(result,group.pads);
		}
		return result;
	}

	private void addPadsTo(ArrayList<SongPad> result, SongPad[] pads) {
		for (int i = 0; i < pads.length; i++) {
			SongPad songPad = pads[i];
			result.add(songPad);
		}

	}

}
