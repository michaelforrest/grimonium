package grimonium.gui;


import java.util.ArrayList;

import grimonium.set.CCEncoder;
import grimonium.set.Colours;
import processing.core.PApplet;

public class EncodersView extends ViewBase{

	private static final int WIDTH = 40;
	private final ArrayList<CCEncoder> ccEncoders;

	public EncodersView(PApplet applet, ArrayList<CCEncoder> arrayList) {
		super(applet);
		this.ccEncoders = arrayList;
	}

	public void draw() {
		applet.pushMatrix();
		for (CCEncoder ccEncoder : ccEncoders) {
			int x = ccEncoder.id * WIDTH;
			drawEncoder(ccEncoder, x);
		}
		applet.popMatrix();
	}

	private void drawEncoder(CCEncoder ccEncoder, int x) {
		applet.fill(0xFF000000 + Colours.get("green"));
		applet.ellipseMode( PApplet.CORNER);
		applet.ellipse(x, 0, 40, 40);
		applet.textFont(font, 12);
		applet.text(ccEncoder.getLabel(), x, 40, WIDTH, 30);
	}

}
