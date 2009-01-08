package rwmidi;

public class PitchBend extends MidiEvent {

	private int value;
	public PitchBend(int midiChannel, int midiData1, int midiData2) {
		super(midiChannel);//, _number, _value);
// to 8192
		//System.out.println(midiData1 + "|" + midiData2);
		value =  (midiData1 + 128 * midiData2) - 0x2000;

	}
	public int getValue(){
		return value;
	}

}
