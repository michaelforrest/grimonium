package grimonium.gui;

import java.util.Observable;
import java.util.Observer;

import processing.core.PApplet;
import sun.misc.Cleaner;
import grimonium.set.ClipGroup;
import grimonium.set.Song;
import grimonium.set.SongPad;

public class SongView implements Observer {

	private PadsView padsView;
	private final Song song;
	private final PApplet applet;
	private final float z;

	public SongView(PApplet applet, Song song, float z) {
		this.applet = applet;
		this.song = song;
		this.z = z;
		padsView = new PadsView(applet);
		assignPads();
		song.addObserver(this);
	}

	public void draw() {
		applet.pushMatrix();
		applet.translate(0, 0, z);
		padsView.draw();
		applet.popMatrix();
	}
	private void assignPads() {
		ClipGroup[] groups = song.groups;
		for (int i = 0; i < groups.length; i++) {
			ClipGroup clipGroup = groups[i];
			assignPads(clipGroup.getPads());
		}

	}

	private void assignPads(SongPad[] pads) {
		for (int i = 0; i < pads.length; i++) {
			SongPad pad = pads[i];
			padsView.assignPad(pad);
		}

	}

	public void update(Observable o, Object arg) {
		if(arg == Song.ACTIVATED) assignPads();
	}

}
