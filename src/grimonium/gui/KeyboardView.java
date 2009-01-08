package grimonium.gui;

import grimonium.maps.KeyboardMap;
import processing.core.PApplet;
import processing.core.PImage;

public class KeyboardView extends ViewBase{
	/*
	 * untransposed, the first C on the MK is C3 / note 48
	 */
	private static final int[] OCTAVES = { 6, 5, 4, 3, 2	}; // will map to C2, C3 etc..

	private static PImage octave;
	private final SongViewHelper helper;
	private KeyboardMap map;

	public KeyboardView(PApplet applet, SongViewHelper helper) {
		super(applet);
		this.helper = helper;
		map = helper.getKeyboardMap();
		if(octave ==null) octave = applet.loadImage("octave.png");
	}

	public void draw() {
		applet.pushMatrix();
		applet.translate(600,50);
		applet.textAlign(PApplet.LEFT);
		applet.textFont(font,12);
		for (int i = 0; i < OCTAVES.length; i++) {
			drawOctave(i);
			applet.translate(0,octave.height);
		}
		//applet.text("Stuff about the keyboard mappings goes in this column. lorem ipsum dolore sit amet etc. etc. etc.".toUpperCase(), 30,0,100, OCTAVES*octave.height);
		applet.popMatrix();
	}

	private void drawOctave(int index) {
		applet.image(octave, 0, 0);
		int octaveNumber = OCTAVES[index];
		if(map != null)	{
			String text = map.findOctaveLabel(octaveNumber).trim().toUpperCase();
			drawNoteStates(octaveNumber);
			applet.text(text,30,0,1000,100);
		}
	}

	private void drawNoteStates(int octaveNumber) {
		int root = (octaveNumber + 1) * 12;
		int noteHeight = octave.height / 12;
		for(int i=0; i<12; i++){
			if(map.noteList[root + i]) {
				applet.rect(22,(11-i)*noteHeight,5,noteHeight);
			}
		}

	}

}
