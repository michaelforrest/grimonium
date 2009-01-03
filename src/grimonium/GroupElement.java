package grimonium;


public class GroupElement{

	protected boolean active = false;

	public void activate() {
		System.out.println("activating " + this);
		active = true;
	}

	public void deactivate() {
		System.out.println("deactivating " + this);
		active = false;
	}

}
