package grimonium.gui;


import grimonium.set.GuiController;
import grimonium.set.Song;

import java.awt.Image;
import java.util.Observable;
import java.util.Observer;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class LiveryView extends ViewBase implements Observer{
	private PImage image;
	public Animator swooshAnimator;
	private PGraphics swoosh;
	private final SongViewHelper helper;

	public LiveryView(String file, PApplet applet, SongViewHelper helper) {
		super(applet);
		this.helper = helper;
		image = loadOrRetrieveImage(file, applet);
		swooshAnimator = new Animator(0, this);
		swoosh = applet.createGraphics(image.width,image.height,PApplet.P2D);
		helper.song.addObserver(this);
	}

	public void draw(float y, boolean active) {
//		mask.beginDraw();
//		mask.fill(255);
//		mask.ellipse(0,0,300,300);
//		mask.endDraw();
//		//PImage masked = applet.createImage(image.width, image.height, PApplet.ARGB);
		applet.tint(0xCC000000 + helper.song.getSongPads().get(0).group.colour);
		applet.image(image, 0, y);
	//	applet.image(swoosh, 0, y);
	}

	private void updateSwoosh(float f) {
		swoosh.beginDraw();
		swoosh.noStroke();
		float size = 50*f;
		swoosh.ellipse(100 + 100*PApplet.sin(f),100 + 100*PApplet.cos(f), size, size);
		swoosh.endDraw();
		image.mask(swoosh);
	}

	public void update(Observable o, Object arg) {
		if(arg == Song.ACTIVATED && swooshAnimator.currentValue == 0) swooshAnimator.set(PApplet.PI * 2, 30);
		if(o == swooshAnimator){
//			System.out.println("v: " + swooshAnimator.currentValue);
			updateSwoosh(swooshAnimator.currentValue);
			GuiController.getInstance().updateView();
		}
	}

}