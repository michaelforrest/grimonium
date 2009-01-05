package grimonium.gui;

import grimonium.LiveAPI;
import grimonium.set.SongPad;
import microkontrol.controls.LED;
import microkontrol.controls.Pad;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

public class PadView extends ViewBase {

	private static final int FONT_SIZE = 16;
	private static final float MARGIN = 11;
	private static final int OUTLINE_MARGIN = 2;
	public static PFont font;
	public static boolean initialised;
	public static PImage texture;
	private static PImage playing;
	// this isassigned from outside after construction.
	public Rectangle rect;
	//

	private final Pad pad;
	private SongPad songPad;

	private PImage background;

	public PadView(PApplet applet, Pad pad) {
		super(applet);
		this.pad = pad;

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
		applet.tint(0xFF000000 + songPad.group.colour, 0xCC);
		applet.image(texture, rect.x, rect.y);
		if(songPad.isPlaying()) applet.image(playing, rect.x, rect.y);
		
		drawText();
	}

	private void drawText() {
		applet.translate(0, 0, 1);
		applet.textFont(font, FONT_SIZE);
		applet.text(songPad.clipName, rect.x + MARGIN, rect.y+ MARGIN, rect.width - 2* MARGIN, rect.height-2*MARGIN);
		applet.translate(0, 0, -1);
	}


	public void clearSongPad() {
		pad.led.set(LED.OFF);
		songPad = null;
	}

	public static void init(PApplet applet) {
		font = applet.loadFont("HelveticaNeue-CondensedBlack-20.vlw");
		texture = applet.loadImage("padtexture.png");
		playing = applet.loadImage("padplaying.png");
		initialised = true;

	}

}
