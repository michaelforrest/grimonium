package grimonium.set;

import java.util.ArrayList;

import grimonium.GroupElement;
import grimonium.gui.MixerSource;
import microkontrol.MicroKontrol;
import microkontrol.controls.EncoderListener;
import processing.core.PApplet;
import processing.xml.XMLElement;

public class GrimoniumSet extends CollectionWithSingleSelectedItem implements EncoderListener, MixerSource{

	public Song[] songs;
	private MicroKontrol mk;
	private GroupElement[] commonElements;
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
		commonElements = new GroupElement[child.getChildren().length];
		for (int i = 0; i < child.getChildren().length; i++) {
			XMLElement element = child.getChildren()[i];
			GroupElement control = ElementFactory.create(element);
			control.activate();
			commonElements[i] = control;

		}
	}
	private void activateSong(int index) {
		select(songs[index]);
	}
	@Override
	public void select(Object object) {
//		System.out.println("running select method in GrimoniumSet class: current=" + current());
		if(current() != null) current().deactivate();
		super.select(object);
//		System.out.println("Now activate " + current());
		current().activate();
	}
	public Song current() {
		return (Song) super.current();
	}
	private void addSongs(XMLElement[] children) {
		songs = new Song[children.length];
		for (int i = 0; i < children.length; i++) {
			XMLElement element = children[i];
//			PApplet.println(element.getStringAttribute("name"));
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
	public ArrayList<CCEncoder> getCommonEncoders() {
		return GroupElement.collectEncoders(commonElements);

	}
	public ArrayList<GroupFader> getCommonFaders() {
		return GroupElement.collectFaders(commonElements);
	}
	public int getOutlineColour() {
		return 0x99000000 + Colours.get("blue");
	}


}
