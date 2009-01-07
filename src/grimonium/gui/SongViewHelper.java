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
	private static final int INACTIVE_ALPHA = 0x00;
	private static final int ACTIVE_ALPHA = 0xDD;
	public SongViewHelper(Song song) {
		this.song = song;
		song.addObserver(this);
		selectionAnimator = new Animator(INACTIVE_ALPHA,this);
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

	public int getSongColour() {
		return song.colour;
	}

	public ArrayList<CCEncoder> getCommonEncoders() {
		return GroupElement.collectEncoders(song.controls);
	}

	public ArrayList<GroupFader> getCommonFaders() {
		return GroupElement.collectFaders(song.controls);
	}

	public int getOutlineColour() {
		return 0xcc000000 + Colours.get("blue");
	}

	public String getImage() {
		return song.getImage();
	}

	public boolean isInvisible() {

		return getAlpha() == 0;
	}

}
