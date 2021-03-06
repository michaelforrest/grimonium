package grimonium.set;

import java.util.Observable;

import processing.core.PApplet;

public class CollectionWithSingleSelectedItem extends Observable{

	public static final String CHANGED = "changed";
	protected int currentIndex;
	protected Object current;
	protected Object[] collection;
	protected void setCollection(Object[] collection){
		this.collection = collection;
	}
	public void select(Object object){
		for (int i = 0; i < collection.length; i++) {
			Object element = collection[i];
			if(element == object) {
				currentIndex = i;
				setChanged();
				notifyObservers(CHANGED);
			}
		}
	}
	public Object current() {
		return collection[currentIndex];
	}
	protected void changeSelectionByOffset(Integer delta) {
		int index = PApplet.constrain(currentIndex + delta, 0, collection.length-1);
		select(collection[index]);
	}
}
