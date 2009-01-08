package grimonium.maps;

import java.util.Hashtable;

import grimonium.Ableton;
import grimonium.NoteParser;
import grimonium.NoteParser.BadNoteFormatException;
import grimonium.set.GuiController;
import microkontrol.MicroKontrol;
import microkontrol.controls.Button;
import microkontrol.controls.ButtonListener;
import microkontrol.controls.LCD;
import microkontrol.controls.LED;
import processing.xml.XMLElement;

public class ButtonMap extends ControlMap implements ButtonListener {

	private static final String GATE = "gate";
	private static final String TOGGLE = "toggle";

	private static Hashtable<String, ButtonMap> buttons = new Hashtable<String, ButtonMap>();

	private boolean on;
	private Button button;
	private String type;
	private int cc;
	private int channel;
	private String id;
	private String name;
	private Integer note;
	/**
	 * 	<button id="SETTING" name="reverb" channel="2" cc="107" type="gate"/>
	 *	<button id="EXIT" name="delay" channel="2" cc="106" type="toggle"/>
	 */
	public ButtonMap(XMLElement element) {
		// TODO: refactor my approach to CC / note entities  throughout the application
		on = false;
		type = element.getStringAttribute("type", "gate");

		if(element.getStringAttribute("note", null) != null){
			try {
				System.out.println("Created a note element");
				note = NoteParser.getNote(element.getStringAttribute("note"));
			} catch (BadNoteFormatException e) {
				System.out.println(e.getMessage());
			}

		}else{
			cc = element.getIntAttribute("cc");
		}
		channel = element.getIntAttribute("channel") - 1;

		addLCDs(element,LCD.RED);


		id = element.getStringAttribute("id");
		name = element.getStringAttribute("name");
		button = MicroKontrol.getInstance().buttons.get(id);
		button.listen(this);

		buttons.put(id, this);
	}



	public void pressed() {
		if(!active) return;
		GuiController.update();
		if(type.equals(TOGGLE)){
			toggle();
		}else{
			gate();
		}
	}

	private void gate() {
		if(note == null){
			Ableton.sendCC(channel,cc, 127);
		}else{
			Ableton.sendNoteOn(channel, note, 127);
		}
		button.led.set(LED.ON);
		turnOnLCDHints();
		on = true;
	}

	private void toggle() {
		on = !on;
		button.led.set( on ? LED.ON : LED.OFF);

		if(note == null){
			Ableton.sendCC(channel,cc, on ? 127 : 0);
		}

		if(on) {
			if(note != null) Ableton.sendNoteOn(channel, note, 127);
			turnOnLCDHints();
		}else{
			if(note != null) Ableton.sendNoteOff(channel, note, 127);
			turnOffLCDHints();
		}
	}

	public void released() {
		if(!active) return;
		GuiController.update();
		if(type.equals(GATE)){
			if(note != null){
				Ableton.sendNoteOff(channel, note, 64);
			}else{
				Ableton.sendCC(channel,cc, 0);
			}
			button.led.set(LED.OFF);
			turnOffLCDHints();
			on = false;
		}

	}



	public static ButtonMap findByButtonName(String id) {
		return buttons.get(id);
	}

	public String getLabel() {
		return id.toUpperCase();
	}



	public String getName() {
		return name.toUpperCase();
	}



	public boolean isOn() {
		return on;
	}

}
