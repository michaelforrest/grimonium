package grimonium.gui;

import processing.core.PApplet;
import processing.core.PImage;

public class KeyboardView {

	private static PImage octave;
	private final PApplet applet;
	private final SongViewHelper helper;

	public KeyboardView(PApplet applet, SongViewHelper helper) {
		this.applet = applet;
		this.helper = helper;
		if(octave ==null) octave = applet.loadImage("octave.png");
	}

	public void draw() {
		applet.pushMatrix();
		applet.translate(600,50);
		applet.image(octave, 0, 0);
		applet.popMatrix();
	}

}
