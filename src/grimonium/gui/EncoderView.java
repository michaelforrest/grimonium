package grimonium.gui;

import grimonium.maps.EncoderMap;
import processing.core.PApplet;

public class EncoderView extends ViewBase {

	public EncoderMap ccEncoder;
	public int x;
	private MixerSource helper;

	public EncoderView(PApplet applet, MixerSource source) {
		super(applet);
		this.helper = source;
	}

	public void draw() {
		if (ccEncoder != null) {
			drawEncoder();
		} else {
			drawSpace();
		}
	}

	private void drawSpace() {
		applet.fill(0xFF000000 + Colours.get("green"));
		 applet.ellipseMode( PApplet.CORNER);
		 applet.ellipse(x, 0, 40, 40);

	}

	private void drawEncoder() {
		applet.fill(0xFF000000 + Colours.get("green"));
		drawCylinder(7, 20, 10);
		applet.pushMatrix();
		applet.translate(0, 0, -40);
		applet.fill(0xffffffff);
		applet.textFont(font, 12);
		applet.rotateX(-PApplet.HALF_PI);
		applet.text(ccEncoder.getLabel(), x, -100, EncodersView.WIDTH, 30);
		applet.popMatrix();
	}

	private void drawCylinder(float sides, int radius, float height) {
		float angle = ccEncoder.getValue() * PApplet.TWO_PI;
		float angleIncrement = PApplet.TWO_PI / sides;
		applet.pushMatrix();
		applet.translate(x + radius, radius);
		applet.beginShape(PApplet.QUAD_STRIP);
		for (int i = 0; i < sides + 1; ++i) {

			applet.vertex(radius * PApplet.cos(angle), radius * PApplet.sin(angle), 0);
			applet.vertex(radius * PApplet.cos(angle), radius * PApplet.sin(angle), height);
			angle += angleIncrement;
		}
		applet.endShape();
		applet.popMatrix();

	}

}
