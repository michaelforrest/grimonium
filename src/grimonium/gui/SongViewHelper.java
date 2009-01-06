package grimonium.gui;

import java.util.Observable;
import java.util.Observer;

import grimonium.set.Song;

public class SongViewHelper extends Observable implements Observer{

	final Song song;
	public int z;
	private Animator selectionAnimator;
	private static final int INACTIVE_ALPHA = 0x66;
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
		return (int) (INACTIVE_ALPHA + (float) selectionAnimator.currentValue * (float)(ACTIVE_ALPHA-INACTIVE_ALPHA));
	}

	public int getGroupColour() {
		return song.getSongPads().get(0).group.colour;
	}

}
