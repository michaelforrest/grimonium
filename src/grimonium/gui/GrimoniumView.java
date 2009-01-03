package grimonium.gui;

import grimonium.Grimonium;
import processing.core.PApplet;

public class GrimoniumView {
	private final PApplet applet;
	private final Grimonium grimonium;

	public GrimoniumView(PApplet applet, Grimonium grimonium) {
		this.applet = applet;
		this.grimonium = grimonium;
		this.applet.registerDraw(this);
	}
	public void draw(){
		applet.background(0);
		//applet.rect(0, 0, applet.width, applet.random(100));
	}
}
