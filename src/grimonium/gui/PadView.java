package grimonium.gui;

import java.util.Observable;
import java.util.Observer;

import grimonium.LiveAPI;
import grimonium.set.SongPad;
import microkontrol.controls.LED;
import microkontrol.controls.Pad;
import processing.core.PApplet;
import processing.core.PImage;

public class PadView extends ViewBase implements Observer {

	private static final int FONT_SIZE = 16;
	private static final float MARGIN = 11;
	private static final int OUTLINE_MARGIN = 2;

	public static boolean initialised;
	public static PImage texture;
	private static PImage playing;
	// this isassigned from outside after construction.
	public Rectangle rect;
	//

	private final Pad pad;
	private SongPad songPad;

	private PImage background;
	private final SongViewHelper helper;

	public PadView(PApplet applet, Pad pad, SongViewHelper helper) {
		super(applet);
		this.pad = pad;
		this.helper = helper;

	}

	public void setSongPad(SongPad songPad) {
		this.songPad = songPad;


		LiveAPI.getClipName(songPad.track, songPad.scene, songPad);
		// background = applet.createGraphics(80,80,PApplet.RGB);
		// background.
		// background.blend(texture, 0, 0, 80, 80, 0, 0, 80, 80,
		// PApplet.MULTIPLY);
	}

	public void draw(){
		if(songPad == null ) return;
		// TODO: trigger animation when song selected / deselected
		applet.tint(0xFF000000 + songPad.group.colour, helper.getAlpha());
		applet.image(texture, rect.x, rect.y);
		if(songPad.isPlaying()) applet.image(playing, rect.x, rect.y);

		drawText();
	}

	private void drawText() {
		applet.fill(0xFFFFFFFF,helper.getTint());
		applet.textFont(font, FONT_SIZE);
		applet.text(songPad.clipName, rect.x + MARGIN, rect.y+ MARGIN, rect.width - 2* MARGIN, rect.height-2*MARGIN, 1);
	}


	public void clearSongPad() {
		pad.led.set(LED.OFF);
		songPad = null;
	}

	public static void init(PApplet applet) {

		texture = applet.loadImage("app/padtexture.png");
		playing = applet.loadImage("app/padplaying.png");
		initialised = true;

	}

	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

}
