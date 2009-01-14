package grimonium.gui;

import grimonium.maps.EncoderMap;
import grimonium.maps.FaderMap;

import java.util.ArrayList;

public interface MixerSource {

	ArrayList<EncoderMap> getEncoders();

	ArrayList<FaderMap> getFaders();

	int getAlpha();

	float getTint();

}
