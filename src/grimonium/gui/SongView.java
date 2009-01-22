package grimonium.gui;

import grimonium.set.ClipGroup;
import grimonium.set.Song;
import grimonium.set.SongPad;

import java.util.Observable;
import java.util.Observer;

import processing.core.PApplet;
import processing.core.PFont;

public class SongView implements Observer {

	private static final float PADS_MARGIN = .12f;
	private static PFont TITLE;
	private PadsView padsView;
	private final Song song;
	private final PApplet applet;
	private LiveryView livery;
	private final SongViewHelper helper;
	private KeyboardView keyboardView;
	private EncodersView encodersView;
	private FadersView fadersView;
	private MixerView mixerView;
	private SongNotesView songNotesView;

	public SongView(PApplet applet, Song song, SongViewHelper helper) {
		this.applet = applet;
		this.song = song;
		this.helper = helper;
		if(TITLE == null) TITLE = applet.loadFont("app/Zapfino-40.vlw");
		addChildren(applet, helper);
		assignPads();
		song.addObserver(this);
	}

	private void addChildren(PApplet applet, SongViewHelper helper) {
		padsView = new PadsView(applet,helper);
		livery = new LiveryView(applet, helper);
		keyboardView = new KeyboardView(applet, helper);
		mixerView = new MixerView(applet, helper);
		songNotesView = new SongNotesView(applet,helper);
	}

	public void draw() {
		if(helper.isInvisible()) return;
		applet.tint(255,helper.getTint());
		applet.pushMatrix();
		applet.translate(0, 0, helper.z);
		livery.draw(applet.height * PADS_MARGIN);
		applet.textAlign(PApplet.LEFT);
		drawText();
		drawPads();
		keyboardView.draw();
		mixerView.draw();
		songNotesView.draw();
		applet.popMatrix();
	}


	private void drawText() {
		applet.pushMatrix();
		applet.rotate(-PApplet.PI * .5f);
		applet.fill(0xFFFFFFFF,helper.getTint());
		applet.textFont(TITLE,24);
		applet.textLeading(50);
		//applet.textAlign(PApplet.CENTER)l
		applet.text(song.name + "(" + song.tempo + ")",-applet.height, 0.11f*applet.width);
		applet.popMatrix();
	}

	private void drawPads() {
		applet.pushMatrix();
		applet.translate(applet.width * PADS_MARGIN, applet.height*PADS_MARGIN);
		padsView.draw();
		applet.popMatrix();
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
		//if(arg == Song.ACTIVATED) assignPads();
	}

}
