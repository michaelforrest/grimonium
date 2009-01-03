package grimonium.gui;

import grimonium.Grimonium;
import grimonium.set.ClipGroup;
import grimonium.set.GrimoniumSet;
import grimonium.set.GuiController;
import grimonium.set.Song;
import grimonium.set.SongPad;

import java.util.Observable;
import java.util.Observer;

import processing.core.PApplet;

public class GrimoniumView implements Observer {
	private static final int Z_SPACING = 30;
	private final PApplet applet;
	private final Grimonium grimonium;
	private boolean clean = false;
	private GuiController controller;
	private SongView[] songViews;
	private MicroKontrolLights microKontrolLights;

	public GrimoniumView(PApplet applet, Grimonium grimonium) {
		this.applet = applet;
		this.grimonium = grimonium;
		this.applet.registerDraw(this);
		controller = GuiController.getInstance();
		controller.addObserver(this);
		grimonium.set.addObserver(this);
		addSongViews();
		microKontrolLights = new MicroKontrolLights(applet, grimonium);
	}

	private void addSongViews() {
		songViews = new SongView[grimonium.set.songs.length];
		for (int i = 0; i < grimonium.set.songs.length; i++) {
			Song song = grimonium.set.songs[i];
			songViews[i] = new SongView(applet,song, i * Z_SPACING);
		}
	}

	public void draw() {
		if (clean) return;
		applet.background(0);
		applet.pushMatrix();
		//applet.rotateX(1);
		for (int i = 0; i < songViews.length; i++) {
			SongView view = songViews[i];
			view.draw();
		}
		applet.popMatrix();
		clean = true;
	}

	public void update(Observable o, Object arg) {
		if(arg == GrimoniumSet.CHANGED) changeSong();
		if(arg == GuiController.UPDATE_VIEW) refresh();
	}

	private void changeSong() {
		refresh();

	}

	private void refresh() {
		clean = false;
	}
}
