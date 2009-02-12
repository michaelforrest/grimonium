package test;
import grimonium.Grimonium;
import grimonium.LiveAPI;
import grimonium.LiveAPI.ClipDataRequester;
import grimonium.set.Clip;
import grimonium.set.ClipGroup;
import processing.core.PApplet;
import processing.core.PFont;

public class LiveAPITest extends PApplet {

	PFont font;
	Grimonium grimonium;
	private Clip clip;


	public void setup() {
		size(800, 600, OPENGL);
		frameRate(30);
		font = loadFont("app/HelveticaNeue-CondensedBlack-20.vlw");
		grimonium = new Grimonium(this, "config.xml", "mappings.xml");
		clip = new Clip(0,0,new ClipGroup());
		LiveAPI.getClipName(0, 0, clip);
		textFont(font,12);
		
	}

	public void draw() {
		background(0);
		text(clip.clipName,0,0,500,30);
		
	}

	static public void main(String args[]) {
		PApplet.main(new String[] { "--bgcolor=#c0c0c0", "LiveAPITest" });
	}
}
