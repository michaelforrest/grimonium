package grimonium.gui;

import grimonium.Grimonium;
import grimonium.set.GrimoniumSet;

import java.util.Observable;
import java.util.Observer;

import microkontrol.MicroKontrol;
import microkontrol.controls.LED;
import microkontrol.controls.Pad;
import processing.core.PApplet;

public class MicroKontrolLights implements Observer {

	private Pad[] pads;
	private final Grimonium grimonium;

	public MicroKontrolLights(PApplet applet, Grimonium grimonium) {
		this.grimonium = grimonium;
		grimonium.set.addObserver(this);
		pads = MicroKontrol.getInstance().pads;
	}

	public void update(Observable o, Object arg) {
		if(arg == GrimoniumSet.CHANGED) changeSong();

	}

	private void changeSong() {
		turnOffPads();
//		ArrayList<SongPad> songPads = grimonium.set.currentSong().getSongPads();
//		for (SongPad songPad : songPads) {
//			songPad.updateHardwareView();
//		}
	}


	private void turnOffPads() {
		for (int i = 0; i < pads.length; i++) {
			Pad pad = pads[i];
			pad.led.set(LED.OFF);
		}
	}

}
