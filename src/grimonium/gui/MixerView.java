package grimonium.gui;

import processing.core.PApplet;

public class MixerView extends ViewBase {

	private EncodersView encodersView;
	private FadersView fadersView;
	private final MixerSource source;

	public MixerView(PApplet applet, MixerSource source) {
		super(applet);
		this.source = source;
		encodersView = new EncodersView(applet, source.getCommonEncoders());
		fadersView = new FadersView(applet, source.getCommonFaders());
	}
	public void draw(){
		applet.pushMatrix();
		applet.translate(800, GrimoniumView.TOP_MARGIN + 40);
		//applet.fill(source.getColour());
		//applet.rect(0,0,40*8,180);
		encodersView.draw();
		applet.translate(0, 70);
		fadersView.draw(source.getColour());
		applet.popMatrix();
	}

}
