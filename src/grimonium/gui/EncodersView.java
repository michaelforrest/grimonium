package grimonium.gui;


import java.util.ArrayList;

import grimonium.maps.EncoderMap;
import processing.core.PApplet;

public class EncodersView extends ViewBase{

	static final int WIDTH = 40;
	private final ArrayList<EncoderMap> ccEncoders;
	private EncoderView[] views = new EncoderView[8];

	public EncodersView(PApplet applet, ArrayList<EncoderMap> arrayList, MixerSource source) {
		super(applet);
		this.ccEncoders = arrayList;
		for (int i = 0; i < views.length; i++) {
			views[i] = new EncoderView(applet, source);
			views[i].x = i * WIDTH;
		}
		for (EncoderMap ccEncoder : ccEncoders) {
			views[ccEncoder.id].ccEncoder = ccEncoder;
		}

	}

	public void draw() {
		applet.pushMatrix();
		for (EncoderView view : views) {
			view.draw();
		}
		applet.popMatrix();
	}


}
