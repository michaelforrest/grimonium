package grimonium.gui;

import processing.core.PApplet;

public class SongNotesView extends ViewBase {

	private static final float LEFT = 1160;
	private static final float WIDTH = 500;

	public SongNotesView(PApplet applet, SongViewHelper helper) {
		super(applet);
	}

	public void draw() {
		applet.pushMatrix();
		applet.translate(LEFT, 0);
		String text =  "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
		applet.text(text , 0,50,WIDTH,1000);
		applet.popMatrix();
	}

}
