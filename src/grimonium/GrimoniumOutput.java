package grimonium;

import netP5.NetAddress;
import netP5.NetInfo;
import oscP5.OscMessage;
import oscP5.OscP5;
import processing.core.PApplet;

public class GrimoniumOutput {
	private static OscP5 oscP5;
	private static NetAddress net;

	public GrimoniumOutput(int port, PApplet applet) {
		net = new NetAddress(NetInfo.getHostAddress(), 7111);
		oscP5 = new OscP5(applet, 12000);
	}
	public static void visualsChanged(String to){
		OscMessage message = new OscMessage("/visualschanged");
		message.add(to);
		oscP5.send(message, net);
	}
}
