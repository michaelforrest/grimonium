package grimonium.gui;

import grimonium.maps.EncoderMap;
import grimonium.maps.MapBase;
import grimonium.maps.FaderMap;
import grimonium.maps.KeyboardMap;
import grimonium.set.Song;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class SongViewHelper extends Observable implements Observer, MixerSource{

	final Song song;
	public int z;
	private Animator selectionAnimator;
	private static final int ACTIVE_ALPHA = 0xDD;
	public SongViewHelper(Song song) {
		this.song = song;
		song.addObserver(this);
		selectionAnimator = new Animator(0,this);
	}

	public void update(Observable o, Object arg) {
		if(o == song){
			setChanged();
			notifyObservers(arg);
			selectionAnimator.set( (arg == Song.ACTIVATED) ? 1 : 0, 10);
		}
	}

	public float getTint() {
		return getAlpha();
	}

	public int getAlpha() {
		return  (int)((float) selectionAnimator.currentValue * (float) ACTIVE_ALPHA);
	}

	public int getGroupColour() {
		return song.getSongPads().get(0).group.colour;
	}

	public int getSongColour() {
		return song.colour;
	}

	public ArrayList<EncoderMap> getEncoders() {
		return MapBase.collectEncoders(song.controls);
	}

	public ArrayList<FaderMap> getFaders() {
		return MapBase.collectFaders(song.controls);
	}


	public String getImage() {
		return song.getImage();
	}

	public boolean isInvisible() {

		return getAlpha() == 0;
	}
	// TODO: work out wtf is going on with these minus numbers...
	public int getColourWithAlpha(int colour) {
		return (1-getAlpha()) * 0xFF000000 + colour;

	}
	public int getColour() {
		return Colours.get("blue") + ((1-getAlpha()) * 0xFF000000);
	}

	public KeyboardMap getKeyboardMap() {
		return MapBase.findKeyboard(song.controls);
	}

}
