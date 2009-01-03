package grimonium.gui;

import processing.core.PApplet;
import processing.core.PFont;
import grimonium.LiveAPI;
import grimonium.set.SongPad;
import microkontrol.controls.Pad;

public class PadView {

	public static PFont font;
	// these are assigned from outside after construction.
	public Point p;
	public Rectangle rect;
	//



	private final Pad pad;
	private SongPad songPad;
	private String clipName = "dunno";
	private final PApplet applet;

	public PadView(PApplet applet, Pad pad) {
		this.applet = applet;
		this.pad = pad;

	}

	public void setSongPad(SongPad songPad) {
		this.songPad = songPad;
		clipName = LiveAPI.getClipName(songPad.track, songPad.scene);

	}

	public void draw() {
		applet.fill(255);
		applet.textFont(font, 20);
		applet.text(clipName);
	}

}
