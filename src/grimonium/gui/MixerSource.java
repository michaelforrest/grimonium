package grimonium.gui;

import grimonium.set.CCEncoder;
import grimonium.set.GroupFader;

import java.util.ArrayList;

public interface MixerSource {

	ArrayList<CCEncoder> getCommonEncoders();

	ArrayList<GroupFader> getCommonFaders();

	int getOutlineColour();

}
