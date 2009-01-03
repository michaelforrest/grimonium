package grimonium.gui;

import grimonium.LiveAPI;
import grimonium.set.SongPad;
import microkontrol.controls.Pad;
import processing.core.PApplet;
import processing.core.PFont;

public class PadView  {

	public static PFont font;
	// this isassigned from outside after construction.
	public Rectangle rect;
	//

	private final Pad pad;
	private SongPad songPad;

	private final PApplet applet;

	public PadView(PApplet applet, Pad pad) {
		this.applet = applet;
		this.pad = pad;

	}

	public void setSongPad(SongPad songPad) {
		this.songPad = songPad;
		LiveAPI.getClipName(songPad.track, songPad.scene, songPad);

	}

	public void draw() {
		applet.fill(50);
		applet.rect(rect.x, rect.y, rect.width, rect.height);
		applet.fill(255);
		if(songPad == null) return;
		applet.textFont(font, 20);
		applet.text(songPad.clipName, rect.x, rect.y,rect.width,rect.height);
	}

	public void clearSongPad() {
		songPad = null;
	}

}
