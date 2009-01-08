package grimonium.set;

import grimonium.gui.Colours;
import grimonium.gui.MixerSource;
import grimonium.maps.EncoderMap;
import grimonium.maps.ElementFactory;
import grimonium.maps.ControlMap;
import grimonium.maps.FaderMap;

import java.util.ArrayList;

import microkontrol.MicroKontrol;
import microkontrol.controls.EncoderListener;
import processing.xml.XMLElement;

public class GrimoniumSet extends CollectionWithSingleSelectedItem implements EncoderListener, MixerSource{

	public Song[] songs;
	private MicroKontrol mk;
	private ControlMap[] commonElements;
	public GrimoniumSet(XMLElement child) {
		if(!child.getName().equals("set")) return;
		mk = MicroKontrol.getInstance();
		addCommon(child.getChild("common"));
		addSongs(child.getChildren("songs/song"));
		setCollection(songs);
		activateSong(0);
		mk.encoders[8].listen(this);
	}
	private void addCommon(XMLElement child) {
		commonElements = new ControlMap[child.getChildren().length];
		for (int i = 0; i < child.getChildren().length; i++) {
			XMLElement element = child.getChildren()[i];
			ControlMap control = ElementFactory.create(element);
			control.activate();
			commonElements[i] = control;
		}
	}
	private void activateSong(int index) {
		select(songs[index]);
	}
	@Override
	public void select(Object object) {
		if(current() != null) current().deactivate();
		super.select(object);
		current().activate();
	}
	public Song current() {
		return (Song) super.current();
	}
	private void addSongs(XMLElement[] children) {
		songs = new Song[children.length];
		for (int i = 0; i < children.length; i++) {
			XMLElement element = children[i];
			Song song = new Song(element);
			songs[i] = song;
		}
	}
	public void moved(Integer delta) {
		changeSelectionByOffset(delta);
	}
	public Song currentSong() {
		return current();
	}
	public ArrayList<EncoderMap> getEncoders() {
		return ControlMap.collectEncoders(commonElements);

	}
	public ArrayList<FaderMap> getFaders() {
		return ControlMap.collectFaders(commonElements);
	}
	public int getColour() {
		return 0x22000000 + Colours.get("blue");
	}
	public void previousSong() {
		changeSelectionByOffset(-1);
	}
	public void nextSong() {
		changeSelectionByOffset(1);

	}


}
