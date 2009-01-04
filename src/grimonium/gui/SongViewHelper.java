package grimonium.gui;

import java.util.Observable;
import java.util.Observer;

import grimonium.set.Song;

public class SongViewHelper extends Observable implements Observer{

	final Song song;
	public int z;

	public SongViewHelper(Song song) {
		this.song = song;
		song.addObserver(this);
	}

	public void update(Observable o, Object arg) {
		setChanged();
		notifyObservers(arg);
	}

}
