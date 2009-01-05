package grimonium.gui;

import java.util.Observable;
import java.util.Observer;

import processing.core.PApplet;
import processing.core.PFont;
import sun.misc.Cleaner;
import grimonium.set.ClipGroup;
import grimonium.set.Song;
import grimonium.set.SongPad;

public class SongView implements Observer {

	private static final float PADS_MARGIN = .12f;
	private static PFont TITLE;
	private PadsView padsView;
	private final Song song;
	private final PApplet applet;
	private LiveryView livery;
	private final SongViewHelper helper;

	public SongView(PApplet applet, Song song, SongViewHelper helper) {
		this.applet = applet;
		this.song = song;
		this.helper = helper;
		if(TITLE==null) TITLE = applet.loadFont("Zapfino-40.vlw");
		padsView = new PadsView(applet);
		livery = new LiveryView("gramophone1.png", applet, helper);
		assignPads();
		song.addObserver(this);
	}

	public void draw() {
		applet.tint(255,(song.isActive() ? 255 : 100));
		applet.pushMatrix();
		applet.translate(0, 0, helper.z);
		livery.draw(applet.height * PADS_MARGIN, song.isActive());
		drawText();
		drawPads();
		applet.popMatrix();
	}

	private void drawText() {
		applet.pushMatrix();
		applet.rotate(-PApplet.PI * .5f);
		applet.textFont(TITLE,40);
		applet.textLeading(50);
		applet.text(song.name,-applet.height, 0.11f*applet.width);
		applet.popMatrix();
	}

	private void drawPads() {
		applet.pushMatrix();
		applet.translate(applet.width * PADS_MARGIN, applet.height*PADS_MARGIN);
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
