package grimonium.gui;

import java.awt.event.KeyEvent;

import microkontrol.MicroKontrol;

import grimonium.set.GrimoniumSet;
import processing.core.PApplet;

public class KeyboardController {

	private final GrimoniumSet set;
	private char[] PADS = {'1','2','3','4',
							'q','w','e','r',
							'a','s','d','f',
							 'z','x','c','v'};

	public KeyboardController(PApplet applet, GrimoniumSet set) {
		this.set = set;
		applet.registerKeyEvent(this);
	}

	public void keyEvent(KeyEvent key) {
		if (key.getID() == KeyEvent.KEY_PRESSED) {
			doKeyPress(key);
		}
	}

	private void doKeyPress(KeyEvent key) {
		int code = key.getKeyCode();
		if(code==KeyEvent.VK_SPACE) MicroKontrol.getInstance().buttons.get("ENTER").press();
		if(code==KeyEvent.VK_UP) set.previousSong();
		if(code==KeyEvent.VK_DOWN) set.nextSong();
		for (int i = 0; i < PADS.length; i++) {
			if(key.getKeyChar() == PADS[i])
				MicroKontrol.getInstance().pads[i].press();
		}

	}
}
