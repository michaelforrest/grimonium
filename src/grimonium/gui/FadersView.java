package grimonium.gui;

import grimonium.maps.FaderMap;

import java.util.ArrayList;

import processing.core.PApplet;

public class FadersView extends ViewBase {

	private static final int WIDTH = 40;
	private static final float HEIGHT = 100;
	private static final float THUMB_HEIGHT = 5;
	private final ArrayList<FaderMap> faders;

	public FadersView(PApplet applet, ArrayList<FaderMap> arrayList) {
		super(applet);
		this.faders = arrayList;
	}

	public void draw(int background) {
		applet.pushMatrix();
		for (FaderMap fader : faders) {
			int x = fader.id * WIDTH;
			applet.fill(background);
			drawFader(fader,x);
		}
		applet.popMatrix();

	}

	private void drawFader(FaderMap fader, int x) {
		applet.rect(x, 0, WIDTH, HEIGHT);
		applet.fill(0xFF000000 + Colours.get("purple"));
		applet.rect(x, (1-fader.value) * HEIGHT-THUMB_HEIGHT,WIDTH, THUMB_HEIGHT );
		applet.textFont(font, 12);
		applet.text(fader.getLabel(), x, HEIGHT, WIDTH, 30);
	}

}
