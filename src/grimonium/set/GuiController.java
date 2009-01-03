package grimonium.set;

import java.util.Observable;

public class GuiController extends Observable {

	public static final String UPDATE_VIEW = "update_view";
	private static GuiController instance;
	static{
		instance = new GuiController();
	}
	public static GuiController getInstance(){
		return instance;
	}
	public void updateView() {
		setChanged();

		notifyObservers(UPDATE_VIEW);
	}

}
