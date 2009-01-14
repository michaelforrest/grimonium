package grimonium.gui;

import grimonium.maps.SongNotes;
import processing.core.PApplet;

public class SongNotesView extends ViewBase {

	private static final float LEFT = 1160;
	private static final float WIDTH = 500;
	private final SongViewHelper helper;
	private SongNotes notes;


	public SongNotesView(PApplet applet, SongViewHelper helper) {
		super(applet);
		this.helper = helper;
		notes = helper.song.getSongNotes();
	}

	public void draw() {
		applet.pushMatrix();
		applet.tint(0xFFFFFFFF,helper.getTint());
		applet.fill(helper.getColourWithAlpha(0xFFFFFF));
		applet.translate(LEFT, 0);
		if(notes.hasImage()) applet.image(notes.getImage(applet),0,0);
		applet.textFont(font,18);
		applet.text(notes.text);
		applet.popMatrix();
	}


}
