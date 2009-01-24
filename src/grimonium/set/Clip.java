package grimonium.set;

import grimonium.Ableton;
import grimonium.LiveAPI;
import grimonium.maps.MapBase;
import microkontrol.MicroKontrol;
import microkontrol.controls.ButtonListener;
import microkontrol.controls.LED;
import microkontrol.controls.Pad;
import processing.xml.XMLElement;

public class Clip extends MapBase implements ButtonListener{
	public static final String CHANGED = "changed";
	public int pad_id ;
	public int scene;
	public int track;
	public String clipName = "Some Clip Not Found";
	public Pad pad;
	// TODO: playing should come from the clipdata
	private boolean playing =  false;
	public final ClipGroup group;
	private ClipMap[] visuals;
	public Clip(XMLElement element, ClipGroup group) {
		this.group = group;
		scene = element.getIntAttribute("scene");
		track = element.getIntAttribute("track"); //added automatically by parent class

		addVisuals(element.getChildren("visual"));

		//setPad(element);
	}

	public void setPad(int pad_id) {
		this.pad_id = pad_id;
		pad = MicroKontrol.getInstance().pads[pad_id];
		pad.listen(this);
	}

	private void addVisuals(XMLElement[] children) {
		visuals = new ClipMap[children.length];
		for (int i = 0; i < children.length; i++) {
			XMLElement element = children[i];
			ClipMap clipMap = new ClipMap(element);
			visuals[i] = clipMap;
		}
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
			stopClips();
		}else{
			playClips();
		}
		playing = !playing;
		updateHardwareView();
	}

	private void playClips() {
		group.clearAllPlaying();
		LiveAPI.trigger(track, scene);
		for (ClipMap visual : visuals) {
			visual.play();
		}
	}

	private void stopClips() {
		Ableton.stopTrack(track);
		stopVisuals();
	}

	private void stopVisuals() {
		for (ClipMap visual : visuals) {
			visual.stop();
		}
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
		stopVisuals();
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
