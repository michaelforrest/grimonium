package grimonium.set;

import grimonium.gui.Colours;
import grimonium.maps.ElementFactory;
import grimonium.maps.MapBase;
import grimonium.maps.SongNotes;

import java.util.ArrayList;
import java.util.Observable;

import microkontrol.MicroKontrol;
import microkontrol.controls.LCD;
import processing.xml.XMLElement;

public class Song extends Observable {

	public static final String ACTIVATED = "activated";
	public static final String DEACTIVATED = "deactivated";
	public String name;
	private Scene stage;
	public ClipGroup[] groups;
	private int sceneOffset;
	private boolean active;
	public int colour;
	public MapBase[] controls;
	private String image;
	private SongNotes notes;
	public int tempo;
	public String visuals;

	public Song(XMLElement element) {
		name = element.getStringAttribute("name");
		stage = new Scene(element.getChild("stage"));
		sceneOffset = element.getIntAttribute("sceneoffset");
		tempo = element.getIntAttribute("tempo");
		colour = Colours.get(element.getAttribute("colour"));
		image = element.getAttribute("pic", "app/gramophone1.png");
		visuals = element.getStringAttribute("visuals", "forest");
		addGroups(element.getChildren("group"));
		arrangePads();

		addControls(element.getChild("controls"));

		notes = new SongNotes(element.getChild("notes"));
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

	private void arrangePads() {
		boolean[] filled = new boolean[16];
		int[] order = {0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15};
		int id = 0;
		for (ClipGroup group : groups) {
			for (SongPad pad : group.pads) {
				pad.setPad(order[id++]);
			}
		}
	}

	private void addControls(XMLElement child) {
		if (child == null) {
			controls = new MapBase[0];
			return;
		}
		XMLElement[] children = child.getChildren();
		controls = new MapBase[children.length];
		for (int i = 0; i < children.length; i++) {
			XMLElement element = children[i];
			controls[i] = ElementFactory.create(element);
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

		for (ClipGroup group : groups)
			group.activate();
		for (MapBase control : controls)
			control.activate();

		setActive(true);
		setChanged();
		notifyObservers(ACTIVATED);
	}

	public void deactivate() {
		for (ClipGroup group : groups)
			group.deactivate();
		for (MapBase control : controls)
			control.deactivate();
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
			addPadsTo(result, group.pads);
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

	public String getImage() {
		return image;
	}

	public SongNotes getSongNotes() {
		return notes;
	}

}
