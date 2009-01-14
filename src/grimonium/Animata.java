package grimonium;

import oscP5.OscMessage;
import oscP5.OscP5;
import processing.core.PApplet;
import netP5.NetAddress;

public class Animata {

	public static NetAddress net;
	public static OscP5 oscP5;

	public static void zoomCamera(Float delta){
		  OscMessage message = new OscMessage("/cameradeltazoom");
		  message.add(delta);
		  oscP5.send(message, net);
		  System.out.println("message:" +message + " to  " + net);
		}

	public static void panLayer(Float deltaX) {
		OscMessage message = new OscMessage("/cameradeltapan");
		message.add(deltaX);
		message.add(0.0f);
		oscP5.send(message, net);
		System.out.println("message:" +message + " to  " + net);
	}

	public static void setBone(String name, Float n) {
		if(net == null) PApplet.println("need to call init() with valid settings on Animata");
		OscMessage message = new OscMessage("/anibone");
		message.add(name);
		message.add((float)n);
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

	public static void setBoneTempo(String bone, Float tempo) {
		if(net == null) PApplet.println("need to call init() with valid settings on Animata");
		OscMessage message = new OscMessage("/bonetempo");
		message.add(bone);
		message.add((float)tempo);
		PApplet.println("trying to send to " + net.address() + " = " + tempo + " to bone " + bone);
		oscP5.send(message, net);
	}

}
