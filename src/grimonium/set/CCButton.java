package grimonium.set;

import java.util.Hashtable;

import grimonium.Ableton;
import grimonium.GroupElement;
import microkontrol.MicroKontrol;
import microkontrol.controls.Button;
import microkontrol.controls.ButtonListener;
import microkontrol.controls.LCD;
import microkontrol.controls.LED;
import processing.xml.XMLElement;

public class CCButton extends GroupElement implements ButtonListener {

	private static final String GATE = "gate";
	private static final String TOGGLE = "toggle";

	private static Hashtable<String, CCButton> buttons = new Hashtable<String, CCButton>();

	private boolean on;
	private Button button;
	private String type;
	private int cc;
	private int channel;
	private String id;
	private String name;
	/**
	 * 	<button id="SETTING" name="reverb" channel="2" cc="107" type="gate"/>
	 *	<button id="EXIT" name="delay" channel="2" cc="106" type="toggle"/>
	 */
	public CCButton(XMLElement element) {

		on = false;
		type = element.getStringAttribute("type");
		channel = element.getIntAttribute("channel") - 1;
		cc = element.getIntAttribute("cc");

		addLCDs(element.getChildren("lcd"),LCD.RED);


		id = element.getStringAttribute("id");
		name = element.getStringAttribute("name");
		button = MicroKontrol.getInstance().buttons.get(id);
		button.listen(this);

		buttons.put(id, this);
	}



	public void pressed() {
		GuiController.update();
		if(type.equals(TOGGLE)){
			toggle();
		}else{
			gate();
		}
	}

	private void gate() {
		Ableton.sendCC(channel,cc, 127);
		button.led.set(LED.ON);
		turnOnLCDHints();
		on = true;
	}

	private void toggle() {
		on = !on;
		button.led.set( on ? LED.ON : LED.OFF);
		Ableton.sendCC(channel,cc, on ? 127 : 0);
		if(on) {
			turnOnLCDHints();
		}else{
			turnOffLCDHints();
		}
	}

	public void released() {
		GuiController.update();
		if(type.equals(GATE)){
			Ableton.sendCC(channel,cc, 0);
			button.led.set(LED.OFF);
			turnOffLCDHints();
			on = false;
		}

	}



	public static CCButton findByButtonName(String id) {
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
