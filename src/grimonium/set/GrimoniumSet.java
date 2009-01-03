package grimonium.set;

import microkontrol.MicroKontrol;
import microkontrol.controls.EncoderListener;
import processing.core.PApplet;
import processing.xml.XMLElement;

public class GrimoniumSet extends CollectionWithSingleSelectedItem implements EncoderListener {

	private Song[] songs;
	private MicroKontrol mk;
	public GrimoniumSet(XMLElement child) {
		if(!child.getName().equals("set")) return;
		addSongs(child.getChildren("songs/song"));
		setCollection(songs);
		activateSong(0);
		mk = MicroKontrol.getInstance();
		mk.encoders[8].listen(this);
	}
	private void activateSong(int index) {
		select(songs[index]);
	}
	public void select(Song song){
		super.select(song);
		current().activate();
	}
	public Song current() {
		return (Song) super.current();
	}
	private void addSongs(XMLElement[] children) {
		songs = new Song[children.length];
		for (int i = 0; i < children.length; i++) {
			XMLElement element = children[i];
			PApplet.println(element.getStringAttribute("name"));
			Song song = new Song(element);
			songs[i] = song;
		}
	}
	public void moved(Integer delta) {
		changeSelectionByOffset(delta);
	}

}
