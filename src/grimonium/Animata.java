package grimonium;

import oscP5.OscMessage;
import oscP5.OscP5;
import processing.core.PApplet;
import netP5.NetAddress;

public class Animata {

	public static NetAddress net;
	public static OscP5 oscP5;

	public static void zoomCamera(Integer delta){
		  OscMessage message = new OscMessage("/cameradeltazoom");
		  message.add(delta);
		  oscP5.send(message, net);
		}

	public static void panLayer(float deltaX) {
		OscMessage message = new OscMessage("/cameradeltapan");
		message.add(deltaX);
		message.add(0.0);
		oscP5.send(message, net);
	}

	public static void setBone(String name, float n) {
		if(net == null) PApplet.println("need to call init() with valid settings on Animata");
		OscMessage message = new OscMessage("/anibone");
		message.add(name);
		message.add(n);
		PApplet.println("trying to send to " + net.address() + " = " + n + " to bone " + name);
		oscP5.send(message, net);
	}

	public static void setAlpha(String layer, float value) {
		OscMessage message = new OscMessage("/layeralpha");
		message.add(layer);
		message.add(value);
		oscP5.send(message, net);
	}

	public static void init(NetAddress netAddress, OscP5 osc) {
		net = netAddress;
		oscP5 = osc;
	}

}
