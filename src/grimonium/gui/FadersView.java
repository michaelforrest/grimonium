package grimonium.gui;

import grimonium.set.Colours;
import grimonium.set.GroupFader;

import java.util.ArrayList;

import processing.core.PApplet;

public class FadersView extends ViewBase {

	private static final int WIDTH = 40;
	private static final float HEIGHT = 100;
	private static final float THUMB_HEIGHT = 5;
	private final ArrayList<GroupFader> faders;

	public FadersView(PApplet applet, ArrayList<GroupFader> arrayList) {
		super(applet);
		this.faders = arrayList;
	}

	public void draw() {
		applet.pushMatrix();
		for (GroupFader fader : faders) {
			int x = fader.id * WIDTH;
			drawFader(fader,x);
		}
		applet.popMatrix();

	}

	private void drawFader(GroupFader fader, int x) {
		applet.noFill();
		applet.rect(x, 0, WIDTH, HEIGHT);
		applet.fill(0xFF000000 + Colours.get("purple"));
		applet.rect(x, (1-fader.value) * HEIGHT-THUMB_HEIGHT,WIDTH, THUMB_HEIGHT );
		applet.textFont(font, 12);
		applet.text(fader.getLabel(), x, HEIGHT, WIDTH, 30);
	}

}
