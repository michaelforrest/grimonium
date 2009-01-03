package grimonium.set;

import microkontrol.MicroKontrol;
import microkontrol.controls.ButtonListener;
import microkontrol.controls.Pad;
import grimonium.Ableton;
import grimonium.ClipDataResponder;
import grimonium.GroupElement;
import grimonium.LiveAPI;
import processing.xml.XMLElement;

public class SongPad extends GroupElement implements ClipDataResponder, ButtonListener{
	public static final String CHANGED = "changed";
	public int id ;
	public int scene;
	public int track;
	public String clipName = "dunno";
	private Pad pad;
	// TODO: playing should come from the clipdata
	private boolean playing =  false;
	public SongPad(XMLElement element) {

		id = element.getIntAttribute("id");
		scene = element.getIntAttribute("scene");
		track = element.getIntAttribute("track"); //added automatically by parent class
		pad = MicroKontrol.getInstance().pads[id];

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
	}

	private void toggle() {
		if(playing){
			Ableton.stopTrack(track);
		}else{
			LiveAPI.trigger(track, scene);
		}

		playing = !playing;
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


}
