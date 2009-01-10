package grimonium.gui;

import java.util.Hashtable;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

public class ViewBase {

	protected static Hashtable<String, PImage> images = new Hashtable<String, PImage>();
	public static PFont font;
	protected final PApplet applet;

	public ViewBase(PApplet applet) {
		this.applet = applet;
		if(font == null ) font = applet.loadFont("app/HelveticaNeue-CondensedBlack-20.vlw");
	}

	protected PImage loadOrRetrieveImage(String file, PApplet applet) {
		PImage image = images.get(file);
		if(image == null) {
			image = applet.loadImage(file);
			images.put(file, image);
		}
		return image;
	}

	protected Point getGridPoint(Rectangle first, int i, int columns) {
		return first.topLeft().add(new Point(first.width * (i % columns), first.height * (int) (i / (float) columns)));
	}

}
