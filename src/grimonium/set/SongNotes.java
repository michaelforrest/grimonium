package grimonium.set;

import processing.core.PApplet;
import processing.core.PImage;
import processing.xml.XMLElement;

public class SongNotes {

	private String imageSource = "";
	private PImage image;


	public String text;

	public SongNotes(XMLElement xml) {
		text = xml.getChild("text").getContent();
		if(text == null) text = "";
		if(xml.getChild("image")!=null) imageSource = xml.getChild("image").getStringAttribute("source");
	}

	public boolean hasImage() {
		return imageSource != "";
	}
	public PImage getImage(PApplet applet) {
		if (image == null) image = applet.loadImage(imageSource);
		return image;
	}

}
