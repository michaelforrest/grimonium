package grimonium.gui;

import processing.core.PApplet;
import grimonium.set.SongPad;
import microkontrol.MicroKontrol;

public class PadsView {
	private static final int SIZE = 80;
	public PadView[] padViews;
	private final PApplet applet;

	public PadsView(PApplet applet) {
		this.applet = applet;
		if(!PadView.initialised) PadView.init(applet);
		addPadViews();
	}

	private void addPadViews() {
		MicroKontrol mk = MicroKontrol.getInstance();
		padViews = new PadView[16];
		Rectangle first = new Rectangle(0, 0, SIZE, SIZE);
		for (int i = 0; i < 16; i++) {
			PadView view = new PadView(applet, mk.pads[i]);
			Point p = first.topLeft().add(new Point(first.width * (i % 4), first.height * (int) (i / 4.0)));
			view.rect = new Rectangle(p.x, p.y, first.width, first.height);
			padViews[i] = view;
		}
	}

	public void assignPad(SongPad pad) {
		padViews[pad.id].setSongPad(pad);

	}

	public void draw() {
		for (int i = 0; i < padViews.length; i++) {
			PadView view = padViews[i];
			view.draw();
		}

	}

	public void clearPadAssignments() {
		for (int i = 0; i < padViews.length; i++) {
			PadView padView = padViews[i];
			padView.clearSongPad();
		}

	}

}
