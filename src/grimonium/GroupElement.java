package grimonium;

import processing.core.PApplet;

public class GroupElement {

	public void activate() {
		PApplet.println("Activating " + this);
	}

	public void deactivate() {
		PApplet.println("Deactivating " + this);
	}

}
