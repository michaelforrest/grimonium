package grimonium.gui;

import processing.core.PApplet;
import processing.core.PImage;

public class KeyboardView extends ViewBase{

	private static final int OCTAVES = 5;
	private static PImage octave;
	private final SongViewHelper helper;

	public KeyboardView(PApplet applet, SongViewHelper helper) {
		super(applet);
		this.helper = helper;
		if(octave ==null) octave = applet.loadImage("octave.png");
	}

	public void draw() {
		applet.pushMatrix();
		applet.translate(600,50);
		for (int i = 0; i < OCTAVES; i++) {
			applet.image(octave, 0, i*octave.height);
		}
		applet.textFont(font,12);
		applet.textAlign(PApplet.LEFT);
		applet.text("Stuff about the keyboard mappings goes in this column. lorem ipsum dolore sit amet etc. etc. etc.".toUpperCase(), 30,0,100, OCTAVES*octave.height);
		applet.popMatrix();
	}

}
