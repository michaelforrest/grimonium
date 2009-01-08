package grimonium.gui;

import grimonium.set.CCEncoder;
import grimonium.set.GroupFader;

import java.util.ArrayList;

public interface MixerSource {

	ArrayList<CCEncoder> getEncoders();

	ArrayList<GroupFader> getFaders();

	int getColour();

}
