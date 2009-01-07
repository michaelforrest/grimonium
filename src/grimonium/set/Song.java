package grimonium.set;

import java.util.ArrayList;
import java.util.Observable;

import microkontrol.MicroKontrol;
import microkontrol.controls.LCD;
import processing.xml.XMLElement;

public class Song extends Observable{

	public static final String ACTIVATED = "activated";
	public static final String DEACTIVATED = "deactivated";
	public String name;
	private Scene stage;
	public ClipGroup[] groups;
	private int sceneOffset;
	private boolean active;
	public int colour;

	public Song(XMLElement element) {
		name = element.getStringAttribute("name");
		stage = new Scene(element.getChild("stage"));
		sceneOffset = element.getIntAttribute("sceneoffset");

		colour = Colours.get(element.getAttribute("colour"));

		addGroups(element.getChildren("group"));
	}

	private void addGroups(XMLElement[] children) {
		groups = new ClipGroup[children.length];
		adjustSceneOffsets(children);
		for (int i = 0; i < children.length; i++) {
			XMLElement element = children[i];
			ClipGroup group = new ClipGroup(element);
			groups[i] = group;
		}

	}

	private void adjustSceneOffsets(XMLElement[] groups) {
		for (int i = 0; i < groups.length; i++) {
			XMLElement group = groups[i];
			adjustSceneOffset(group.getChildren("clip"));
		}
	}

	private void adjustSceneOffset(XMLElement[] pads) {
		for (int i = 0; i < pads.length; i++) {
			XMLElement pad = pads[i];
			Integer newOffset = pad.getIntAttribute("scene") + sceneOffset;
			pad.setAttribute("scene", newOffset.toString());
		}
	}

	public void activate() {
		LCD mainLCD = MicroKontrol.getInstance().lcds[8];
		mainLCD.setText(name);
		mainLCD.setColor("orange");
		for (int i = 0; i < groups.length; i++) {
			ClipGroup group = groups[i];
			group.activate();
		}
		setActive(true);
		setChanged();
		notifyObservers(ACTIVATED);
	}
	public void deactivate(){
		for (int i = 0; i < groups.length; i++) {
			ClipGroup group = groups[i];
			group.deactivate();
		}
		setActive(false);
		setChanged();
		notifyObservers(DEACTIVATED);
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

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}

}
