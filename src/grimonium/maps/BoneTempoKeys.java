package grimonium.maps;

import grimonium.Ableton;
import grimonium.Animata;
import grimonium.Ableton.MidiTrack;

import java.util.Observable;
import java.util.Observer;

import processing.xml.XMLElement;
@Deprecated
public class BoneTempoKeys extends KeyboardRange implements Observer {

	private Float tempo;
	private String bone;
	private int keysPressed = 0;
	private int channel;
	private MidiTrack track;

	public BoneTempoKeys(XMLElement element) {
		super(element);
		bone = element.getStringAttribute("bone");
		tempo = element.getFloatAttribute("tempo");
		System.out.println("created bonetempokey from xml " + element + ": channel" + channel + ", " + bone + " " + tempo);
		listenToAbletonMidi();
	}

	public BoneTempoKeys(int low, int high, String bone, float tempo, int channel) {
		super();
		this.channel = channel;
		this.low = low;
		this.high = high;
		this.bone = bone;
		this.tempo = tempo;
		listenToAbletonMidi();
	}

	private void listenToAbletonMidi() {
		/// TODO: work this out. I am being stupid due to being drunk
		channel = 2;
		System.out.println("listening to Ableton channel " + channel + " with "  + this);
		track = Ableton.midiTracks[channel];
		track.addObserver(this);
	}

	@Override
	protected void listenToMicroKontrol() {
		// don't
	}

	public void update(Observable o, Object arg) {
		int pitch = track.lastNote.getPitch();
		if (pitch < low) return;
		if (pitch > high) return;
		float trigger = (arg.equals("note_on")) ? 0f : 1f;

		Float newTempo = 1f;
		if(arg.equals(Ableton.MidiTrack.NOTE_ON)){
			keysPressed ++;
			newTempo = tempo;
		}else{
			keysPressed--;
			if(keysPressed == 0) newTempo = 0f;
		}

		Animata.setBoneTempo(bone, newTempo);
		Animata.setBone("trigger" + bone, trigger);

	}

}
