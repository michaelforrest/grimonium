package grimonium.gui;

import grimonium.maps.FaderMap;
import processing.core.PApplet;

public class FaderView extends ViewBase {
	public static final int WIDTH = 40;
	private static final float HEIGHT = 100;
	private static final float THUMB_HEIGHT = 5;
	public int x;
	public FaderMap fader;
	private final MixerSource source;

	public FaderView(PApplet applet, MixerSource source) {
		super(applet);
		this.source = source;
	}

	public void draw() {
		applet.tint(0xFFFFFFFF, source.getTint());
		applet.fill(0xFF000000 + Colours.get("blue"));
		applet.rect(x, 0, WIDTH, HEIGHT);
		if(fader!=null) drawFader();
	}
	private void drawFader() {
		int colour = 0xFF000000 + Colours.get("purple");
	
		applet.fill(colour);
		float y = (1-fader.value) * HEIGHT-THUMB_HEIGHT;
		applet.rect(x, y,WIDTH, THUMB_HEIGHT );

		applet.beginShape(PApplet.QUADS);
		int depth = 10;
		applet.vertex(x, y, depth);//WIDTH, THUMB_HEIGHT);
		applet.vertex(x+WIDTH,y,depth);
		float bottom = y+THUMB_HEIGHT;
		applet.vertex(x+WIDTH,bottom, depth);
		applet.vertex(x,bottom,depth);
		applet.endShape();

		applet.beginShape(PApplet.QUADS);
		applet.vertex(x, bottom, depth);//WIDTH, THUMB_HEIGHT);
		applet.vertex(x+WIDTH,bottom,depth);
		applet.vertex(x+WIDTH,bottom, 0);
		applet.vertex(x,bottom,0);
		applet.endShape();

		applet.pushMatrix();
		applet.rotateX(-PApplet.PI *.5f );
		applet.translate(0, 0,105);
		applet.fill(0xFFFFFFFF);
		applet.textFont(font, 12);
		applet.text(fader.getLabel(), x, 0, WIDTH, 30);
		applet.popMatrix();
	}

}
