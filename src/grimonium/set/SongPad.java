package grimonium.set;

import microkontrol.MicroKontrol;
import microkontrol.controls.ButtonListener;
import microkontrol.controls.LED;
import microkontrol.controls.Pad;
import grimonium.Ableton;
import grimonium.ClipDataResponder;
import grimonium.LiveAPI;
import grimonium.maps.MapBase;
import processing.xml.XMLElement;

public class SongPad extends MapBase implements ClipDataResponder, ButtonListener{
	public static final String CHANGED = "changed";
	public int pad_id ;
	public int scene;
	public int track;
	public String clipName = "Some Clip Not Found";
	public Pad pad;
	// TODO: playing should come from the clipdata
	private boolean playing =  false;
	public final ClipGroup group;
	public SongPad(XMLElement element, ClipGroup group) {

		this.group = group;
		pad_id = element.getIntAttribute("pad");
		scene = element.getIntAttribute("scene");
		track = element.getIntAttribute("track"); //added automatically by parent class
		pad = MicroKontrol.getInstance().pads[pad_id];

		pad.listen(this);
	}

	public void setClipName(String string) {
		clipName = string;
		GuiController.getInstance().updateView();
	}

	public void pressed() {
		System.out.println("pressed " + clipName + " active?" + active);
		if(!active) return;
		toggle();
		GuiController.getInstance().updateView();
	}

	private void toggle() {
		if(playing){
			Ableton.stopTrack(track);
		}else{
			group.clearAllPlaying();
			LiveAPI.trigger(track, scene);
		}
		playing = !playing;
		updateHardwareView();
	}


	public void released() {
		if(!active) return;

	}

	public void updated() {
		if(!active) return;

	}
	@Override
	public String toString() {
		return super.toString() + "[" + clipName +"]";
	}

	@Override
	public void activate() {
		super.activate();
		updateHardwareView();
	}
	public void updateHardwareView() {
		if(playing) {
			pad.led.set(LED.BLINK);
		}else{
			pad.led.set(LED.ON);
		}
	}

	public void clearPlaying() {
		playing = false;
		updateHardwareView();

	}

	public boolean isActive() {
		return active;
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setClipTriggered(boolean b) {
		playing = b;
	}




}
