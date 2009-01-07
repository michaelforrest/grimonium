package grimonium.gui;

import grimonium.Grimonium;
import grimonium.set.GrimoniumSet;
import grimonium.set.GuiController;
import grimonium.set.Song;

import java.util.Observable;
import java.util.Observer;

import javax.media.opengl.GL;

import processing.core.PApplet;
import processing.opengl.PGraphicsOpenGL;

public class GrimoniumView extends ViewBase implements Observer {
	private static final int Z_SPACING = 200;
	private final Grimonium grimonium;
	private boolean clean = false;
	private GuiController controller;
	private SongView[] songViews;
	private MicroKontrolLights microKontrolLights;
	private Animator zAnimator;
	private SongViewHelper[] helpers;
	private ButtonsView buttonsView;

	// private PGraphicsOpenGL pGraphicsOpenGL;
	// private GL gl;

	public GrimoniumView(PApplet applet, Grimonium grimonium) {
		super(applet);
		applet.registerDraw(this);

		controller = GuiController.getInstance();
		controller.addObserver(this);

		Animator.init(applet);

		this.grimonium = grimonium;
		grimonium.set.addObserver(this);

		createViewHelpers();
		addSongViews();
		addButtonsView();

		microKontrolLights = new MicroKontrolLights(applet, grimonium);

		zAnimator = new Animator(0f, this);
	}

	private void addButtonsView() {
		buttonsView = new ButtonsView(applet, grimonium);
	}

	private void createViewHelpers() {
		helpers = new SongViewHelper[grimonium.set.songs.length];
		int z = 0;
		for (int i = 0; i < grimonium.set.songs.length; i++) {
			Song song = grimonium.set.songs[i];
			SongViewHelper helper = new SongViewHelper(song);
			helper.z = i * Z_SPACING;
			helper.addObserver(this);
			helpers[i] = helper;
		}

	}

	private void addSongViews() {
		songViews = new SongView[grimonium.set.songs.length];
		for (int i = 0; i < grimonium.set.songs.length; i++) {
			Song song = grimonium.set.songs[i];
			songViews[i] = new SongView(applet, song, helpers[i]);
		}
	}

	public void draw() {
		if (clean) return;
		applet.background(0);
		// applet.lightFalloff(1,0,.015f);
		// applet.pointLight(255, 255, 255, applet.width * .1f, applet.height *
		// .1f, 100 );
		// applet.pointLight(255, 255, 255, applet.width * .2f, applet.height *
		// .1f, 100 );
		applet.pushMatrix();
		applet.translate(0, 0, zAnimator.currentValue);
		buttonsView.draw(-zAnimator.currentValue);
		drawSongViews();
		applet.popMatrix();
		clean = true;
	}

	private void drawSongViews() {
		for (int i = 0; i < songViews.length; i++) {
			SongView view = songViews[i];
			view.draw();
		}
	}

	public void update(Observable o, Object arg) {
		if (arg == GrimoniumSet.CHANGED) changeSong();
		if (arg == GuiController.UPDATE_VIEW) refresh();
		if (arg == Animator.NEXT_FRAME) refresh();
		if (o instanceof SongViewHelper && arg == Song.ACTIVATED) update((SongViewHelper) o);
	}

	private void update(SongViewHelper helper) {
		zAnimator.set(-helper.z, 10);
	}

	private void changeSong() {
		refresh();
	}

	private void refresh() {
		clean = false;
	}
}
