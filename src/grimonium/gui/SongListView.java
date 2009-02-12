package grimonium.gui;

import grimonium.set.GrimoniumSet;
import grimonium.set.Song;
import processing.core.PApplet;

public class SongListView extends ViewBase {

	private GrimoniumSet set;

	public SongListView(PApplet applet, GrimoniumSet set) {
		super(applet);
		this.set = set;
	}

	public void draw() {
		applet.pushMatrix();
		applet.translate(487,70);
		applet.textAlign(PApplet.LEFT);
		for(Song song : set.songs){
			applet.fill( song.isActive() ? 255 : 150 );
			applet.text(song.name,300,0,300,20);
			applet.translate(0, 15);
		}
		applet.popMatrix();
	}

}
