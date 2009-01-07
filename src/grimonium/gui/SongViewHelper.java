package grimonium.gui;

import grimonium.GroupElement;
import grimonium.set.CCEncoder;
import grimonium.set.Colours;
import grimonium.set.GroupFader;
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

	public ArrayList<CCEncoder> getCommonEncoders() {
		return GroupElement.collectEncoders(song.controls);
	}

	public ArrayList<GroupFader> getCommonFaders() {
		return GroupElement.collectFaders(song.controls);
	}

	public int getColour() {
		return Colours.get("blue") + ((1-getAlpha()) * 0xFF000000);
	}

	public String getImage() {
		return song.getImage();
	}

	public boolean isInvisible() {

		return getAlpha() == 0;
	}

}
