package grimonium.gui;

import grimonium.Grimonium;
import grimonium.set.ClipGroup;
import grimonium.set.GrimoniumSet;
import grimonium.set.Song;
import grimonium.set.SongPad;

import java.util.Observable;
import java.util.Observer;

import processing.core.PApplet;

public class GrimoniumView implements Observer {
	private final PApplet applet;
	private final Grimonium grimonium;
	private boolean clean = false;
	private Song song;
	private PadsView padsView;

	public GrimoniumView(PApplet applet, Grimonium grimonium) {
		this.applet = applet;
		this.grimonium = grimonium;
		this.applet.registerDraw(this);
		registerToSet();
		padsView = new PadsView(applet);
	}

	private void registerToSet() {
		grimonium.set.addObserver(this);

	}

	public void draw() {
		if (clean) return;
		applet.background(0);
		padsView.draw();
		clean = true;
	}

	private void assignPads() {
		ClipGroup[] groups = song.groups;
		for (int i = 0; i < groups.length; i++) {
			ClipGroup clipGroup = groups[i];
			assignPads(clipGroup.getPads());
		}

	}

	private void assignPads(SongPad[] pads) {
		for (int i = 0; i < pads.length; i++) {
			SongPad pad = pads[i];
			padsView.assignPad(pad);
		}

	}

	public void update(Observable o, Object arg) {
		if (arg == GrimoniumSet.CHANGED) changeSong();

	}

	private void changeSong() {
		song = grimonium.set.current();
		assignPads();
		refresh();
	}

	private void refresh() {
		clean = false;
	}
}
