package test;
import processing.opengl.*;

import grimonium.Grimonium;
import grimonium.gui.GrimoniumView;
import processing.core.PApplet;
import processing.core.PFont;

public class LiveSet extends PApplet {

	PFont font;
	Grimonium grimonium;
	GrimoniumView view;

	public void setup() {
		System.out.println(System.getProperty("java.class.path"));
		size(1870, 380, OPENGL);
		frameRate(30);
		load();
		font = loadFont("app/HelveticaNeue-CondensedBlack-20.vlw");
	}

	public void load() {
		grimonium = new Grimonium(this, "config.xml", "mappings.xml");
		view = new GrimoniumView(this, grimonium);
		grimonium.set.previousSong();
	}

	public void draw() {

	}

	static public void main(String args[]) {
		PApplet.main(new String[] { "--bgcolor=#c0c0c0", "LiveSet" });
	}
}
