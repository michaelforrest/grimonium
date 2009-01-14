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
	private static final float ACTIVE_ALPHA = 0xDD000000;
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
		return selectionAnimator.currentValue * (float)0xFF;
	}

	public int getAlpha() {
		if(ACTIVE_ALPHA != 0xDD000000) System.out.println("WTF? ACTIV EALPHA is " + ACTIVE_ALPHA);
		int alpha = (int)( selectionAnimator.currentValue * ACTIVE_ALPHA);
		return  alpha;
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
		return getAlpha() + colour;

	}

	public KeyboardMap getKeyboardMap() {
		return MapBase.findKeyboard(song.controls);
	}


}
