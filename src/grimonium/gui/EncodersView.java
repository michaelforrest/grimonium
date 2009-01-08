package grimonium.gui;


import java.util.ArrayList;

import grimonium.maps.EncoderMap;
import processing.core.PApplet;

public class EncodersView extends ViewBase{

	private static final int WIDTH = 40;
	private final ArrayList<EncoderMap> ccEncoders;

	public EncodersView(PApplet applet, ArrayList<EncoderMap> arrayList) {
		super(applet);
		this.ccEncoders = arrayList;
	}

	public void draw() {
		applet.pushMatrix();
		for (EncoderMap ccEncoder : ccEncoders) {
			int x = ccEncoder.id * WIDTH;
			drawEncoder(ccEncoder, x);
		}
		applet.popMatrix();
	}

	private void drawEncoder(EncoderMap ccEncoder, int x) {
		applet.fill(0xFF000000 + Colours.get("green"));
		applet.ellipseMode( PApplet.CORNER);
		applet.ellipse(x, 0, 40, 40);
		applet.textFont(font, 12);
		applet.text(ccEncoder.getLabel(), x, 40, WIDTH, 30);
	}

}
