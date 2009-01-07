package grimonium.set;

import java.util.Hashtable;

public class Colours {
	public static Hashtable<String, Integer> COLOURS = new Hashtable<String, Integer>();
	static {
		COLOURS.put("red", 0xf01229); // red
		COLOURS.put("yellow", 0xffdc06); // yellow
		COLOURS.put("pink", 0xd707a5); // pink
		COLOURS.put("purple", 0x9858e0); //purple
		COLOURS.put("green", 0x3c6a08); // green
		COLOURS.put("brown", 0x8a6636); //brown
	}
	public static int get(String id) {
		int result;
		try{
			result = COLOURS.get(id);
		}catch(Exception e){
			System.out.println("ERROR, for now you need to choose an 'id' from the set: red,yellow,pink,purple,green");
			result = COLOURS.get("brown");
		}
		return result;
	}
}
