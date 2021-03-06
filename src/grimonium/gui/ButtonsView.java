package grimonium.gui;

import microkontrol.MicroKontrol;
import grimonium.Grimonium;
import processing.core.PApplet;

public class ButtonsView extends ViewBase{

	private static final int COLUMNS = 4;
	private static final float LEFT = 771;
	private final Grimonium grimonium;
	private String[] BUTTONS = {"SETTING","MESSAGE","SCENE","EXIT"};
	private ButtonView[] views;

	public ButtonsView(PApplet applet, Grimonium grimonium) {
		super(applet);
		this.grimonium = grimonium;

		addViews();
	}

	private void addViews() {
		Rectangle first = new Rectangle(0, -33, ButtonView.SIZE, ButtonView.HEIGHT);
		views = new ButtonView[BUTTONS.length];
		for (int i = 0; i < BUTTONS.length; i++) {
			String id = BUTTONS[i];
			ButtonView view = new ButtonView(MicroKontrol.getInstance().buttons.get(id), applet, id, grimonium);
			view.setPosition(getGridPoint(first, i, COLUMNS));
			views[i] = view;
		}
	}

	public void draw() {
		applet.pushMatrix();
		applet.translate(LEFT, 50, 0);
		for (ButtonView view : views) {
			view.draw();
		}
		applet.popMatrix();
	}

}
