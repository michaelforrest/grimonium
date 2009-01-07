package grimonium.set;

import java.util.Hashtable;

public class Colours {
	public static Hashtable<String, Integer> COLOURS = new Hashtable<String, Integer>();
	static {
		COLOURS.put("red", 0xf01229);
		COLOURS.put("yellow", 0xffdc06);
		COLOURS.put("pink", 0xd707a5);
		COLOURS.put("purple", 0x9858e0);
		COLOURS.put("green", 0x3c6a08);
		COLOURS.put("blue", 0x3839d2);
		COLOURS.put("brown", 0x8a6636);
	}
	public static int get(String id) {
		int result;
		try{
			result = COLOURS.get(id);
		}catch(Exception e){
			System.out.println("unknown colour " + id);
			result = COLOURS.get("brown");
		}
		return result;
	}
}
