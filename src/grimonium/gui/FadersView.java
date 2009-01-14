package grimonium.gui;

import grimonium.maps.FaderMap;

import java.util.ArrayList;

import processing.core.PApplet;

public class FadersView extends ViewBase {


	private final ArrayList<FaderMap> faders;
	private FaderView[] views = new FaderView[8];


	public FadersView(PApplet applet, ArrayList<FaderMap> arrayList, MixerSource source) {
		super(applet);
		this.faders = arrayList;
		for (int i = 0; i < views.length; i++) {
			FaderView view = new FaderView(applet, source);
			views[i] = view;
			views[i].x = i* FaderView.WIDTH;
		}
		for (FaderMap fader : faders) {
			views[fader.id].fader = fader;
		}

	}

	public void draw() {
		applet.pushMatrix();
		for (FaderView view : views) {
			view.draw();
		}
		applet.popMatrix();

	}



}
