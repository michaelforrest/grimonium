package grimonium.gui;

import grimonium.Grimonium;
import grimonium.set.CCButton;
import grimonium.set.Colours;
import microkontrol.controls.Button;
import processing.core.PApplet;
import processing.core.PImage;

public class ButtonView extends ViewBase{
	public static final float SIZE = 80;
	private static final int WHITE = 0xFFFFFFFF;
	private final Button button;
	private Point position;
	private String name;
	private CCButton ccButton;
	private static PImage background;
	private static PImage highlight;
	private static PImage power;

	public ButtonView(Button button, PApplet applet, String id, Grimonium grimonium) {
		super(applet);
		this.button = button;
		this.name = id;
		this.ccButton = CCButton.findByButtonName(id);
		if(background == null) initGraphics();
	}

	private void initGraphics() {
		background = applet.loadImage("button/background.png");
		highlight = applet.loadImage("button/highlight.png");
		power = applet.loadImage("button/power.png");
	}

	public void draw() {
		applet.pushMatrix();
		applet.translate(position.x,position.y);
		drawImages();
		drawText();
		applet.popMatrix();
	}

	private void drawText() {
		if(ccButton == null) return;
		applet.textFont(font,9);
		applet.textAlign(PApplet.CENTER);
		applet.fill(WHITE);
		applet.text(ccButton.getLabel(), 0,5,SIZE,12);

		applet.textFont(font,12);
		applet.text(ccButton.getName(), 0, 62, SIZE, 15);

	}

	private void drawImages() {
		applet.tint(Colours.get("green") + 0xFF000000);
		applet.image(background, 0, 0);
		highlightIfOn();
		applet.image(power, 0, 0);
	}

	private void highlightIfOn() {
		if(ccButton==null) return;
		if(ccButton.isOn()) {
			applet.tint(0xFFFFFFFF);
			applet.image(highlight, 0, 0);
		}

	}

	public void setPosition(Point p) {
		position = p;

	}

}
