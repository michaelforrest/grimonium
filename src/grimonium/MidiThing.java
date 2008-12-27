package grimonium;

import processing.core.PApplet;
import rwmidi.*;

public class MidiThing {
	protected MidiInput getInput(String name, Object target) {
		return RWMidi.getInputDevice(findInput(name)).createInput(target);
	}

	protected MidiOutput getOutput(String name) {
		String outputName = findOutput(name);

		return RWMidi.getOutputDevice(outputName).createOutput();
	}

	protected  String findInput(String regex) {
		return findInArray(regex, RWMidi.getInputDeviceNames());
	}

	protected String findOutput(String regex) {
		return findInArray(regex, RWMidi.getOutputDeviceNames());
	}

	protected String findInArray(String regex, String[] array) {
		for (int i = 0; i < array.length; i++) {
			String string = array[i];
			if (PApplet.match(string, regex)!=null) return string;
		}
		PApplet.println("Couldn't find device matching " + regex);
		displayInterfaces();
		return null;
	}
	public static void displayInterfaces() {
		String[] inputs = RWMidi.getInputDeviceNames();
		PApplet.println("INPUTS: ");
		PApplet.println(PApplet.join(inputs, "\n"));
		PApplet.println("OUTPUTS: ");
		String[] outputs = RWMidi.getOutputDeviceNames();
		PApplet.println(PApplet.join(outputs,  "\n"));

	}

}
