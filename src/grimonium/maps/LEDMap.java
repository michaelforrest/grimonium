package grimonium.maps;

import processing.xml.XMLElement;

public class LEDMap extends MapBase {

	private LCDHint hint;

	public LEDMap(XMLElement element) {
		hint = new LCDHint(element, "orange");
	}
	@Override
	public void activate() {
		super.activate();
		hint.activateHint();
	}
	@Override
	public void deactivate() {
		hint.revert();
		super.deactivate();

	}
}
