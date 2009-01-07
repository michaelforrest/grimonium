package grimonium.gui;

import processing.core.PApplet;
import grimonium.set.SongPad;
import microkontrol.MicroKontrol;

public class PadsView extends ViewBase{
	private static final int SIZE = 80;
	public PadView[] padViews;
	private SongViewHelper helper;

	public PadsView(PApplet applet, SongViewHelper helper) {
		super(applet);
		this.helper = helper;
		if(!PadView.initialised) PadView.init(applet);
		addPadViews();
	}

	private void addPadViews() {
		MicroKontrol mk = MicroKontrol.getInstance();
		padViews = new PadView[16];
		Rectangle first = new Rectangle(0, 0, SIZE, SIZE);
		for (int i = 0; i < 16; i++) {
			PadView view = new PadView(applet, mk.pads[i],helper);
			Point p = getGridPoint(first, i, 4);
			view.rect = new Rectangle(p.x, p.y, first.width, first.height);
			padViews[i] = view;
		}
	}

	public void assignPad(SongPad pad) {
		padViews[pad.pad_id].setSongPad(pad);
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
