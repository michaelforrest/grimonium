package grimonium.maps;

import grimonium.Ableton;

import java.util.Observable;
import java.util.Observer;

import microkontrol.MicroKontrol;
import microkontrol.controls.Joystick;
import processing.core.PApplet;
import processing.xml.XMLElement;
import rwmidi.PitchBend;

public class JoystickMap extends MapBase implements Observer {

	private int[] pitchBendChannels;
	private Joystick joystick;
	private CC[] right;
	private CC[] up;
	private CC[] down;
	private CC[] left;

	public JoystickMap(XMLElement element) {
		joystick = MicroKontrol.getInstance().joystick;
		joystick.addObserver(this);
		addPitchBend(element.getChildren("pitchbend"));
		addRight(element.getChildren("right"));
		addUp(element.getChildren("up"));
		addLeft(element.getChildren("left"));
		addDown(element.getChildren("down"));
	}

	private void addRight(XMLElement[] children) {
		right = new CC[children.length];
		for (int i = 0; i < children.length; i++) {
			XMLElement element = children[i];
			CC cc = new CC(element);
			right[i] = cc;
		}
	}
	private void addLeft(XMLElement[] children) {
		left = new CC[children.length];
		for (int i = 0; i < children.length; i++) {
			XMLElement element = children[i];
			CC cc = new CC(element);
			left[i] = cc;
		}
	}
	private void addDown(XMLElement[] children) {
		down = new CC[children.length];
		for (int i = 0; i < children.length; i++) {
			XMLElement element = children[i];
			CC cc = new CC(element);
			down[i] = cc;
		}
	}

	private void addUp(XMLElement[] children) {
		up = new CC[children.length];
		for (int i = 0; i < children.length; i++) {
			XMLElement element = children[i];
			CC cc = new CC(element);
			up[i] = cc;
		}
	}

	public void update(Observable o, Object arg) {
		if(!active) return;
		for (CC x : right) {
			x.send((int)( PApplet.constrain(joystick.getX(), 0, 1)  * 127));
		}
		for (CC y : up) {
			y.send((int)(PApplet.constrain(joystick.getY(), 0, 1) * 127));
		}
		for (CC x : left) {
			x.send((int)(-PApplet.constrain(joystick.getX(), -1, 0)  * 127));
		}
		for (CC y : down) {
			y.send((int)(-PApplet.constrain(joystick.getY(), -1, 0) * 127));
		}
	}

	private void addPitchBend(XMLElement[] children) {
		pitchBendChannels = new int[children.length];
		if(children.length == 0) return;
		MicroKontrol.getInstance().plugPitchBend(this);
		for (int i = 0; i < children.length; i++) {
			XMLElement element = children[i];
			pitchBendChannels[i] = element.getIntAttribute("channel") - 1;
		}
	}

	public void pitchBendReceived(PitchBend msg){
		if(!active) return;
		for (int i = 0; i < pitchBendChannels.length; i++) {
			int channel = pitchBendChannels[i];
			Ableton.getInstance().to.sendPitchBend(channel, msg);
		}
	}

}
