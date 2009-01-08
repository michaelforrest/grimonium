package rwmidi;

public class PitchBend extends MidiEvent {

	public PitchBend(int midiChannel, int midiData1, int midiData2) {
		super(midiChannel);//, _number, _value);

	}
	public int getValue(){
		return getData1()*0xFFFFFFFF + getData2();
	}

}
