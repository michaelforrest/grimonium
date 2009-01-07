package grimonium.gui;

import java.util.Hashtable;

import javax.media.opengl.GL;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.opengl.PGraphicsOpenGL;

public class ViewBase {

	protected static Hashtable<String, PImage> images = new Hashtable<String, PImage>();
	public static PFont font;
	protected final PApplet applet;

	public ViewBase(PApplet applet) {
		this.applet = applet;
		if(font == null ) font = applet.loadFont("HelveticaNeue-CondensedBlack-20.vlw");
	}

	protected void setupLights() {
		PGraphicsOpenGL pgl = (PGraphicsOpenGL) applet.g;
		GL gl = pgl.beginGL();
		gl.glLightf(GL.GL_LIGHT0, GL.GL_CONSTANT_ATTENUATION, 0);
	    gl.glLightf(GL.GL_LIGHT0, GL.GL_LINEAR_ATTENUATION, 0.8f);
	    gl.glLightf(GL.GL_LIGHT0, GL.GL_QUADRATIC_ATTENUATION, 0.8f);
		pgl.endGL();
	}

	public void multiply() {
		PGraphicsOpenGL pgl = (PGraphicsOpenGL) applet.g;
		GL gl = pgl.beginGL();
		gl.glEnable(GL.GL_BLEND);
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glBlendFunc(GL.GL_DST_COLOR, GL.GL_ZERO);
		pgl.endGL();
	}

	public void noBlend() {
		GL gl = ((PGraphicsOpenGL) applet.g).beginGL();
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		// gl.glDisable(GL.GL_BLEND);
		((PGraphicsOpenGL) applet.g).endGL();
	}

	protected PImage loadOrRetrieveImage(String file, PApplet applet) {
		PImage image = images.get(file);
		if(image == null) {
			image = applet.loadImage(file);
			images.put(file, image);
		}
		return image;
	}

	protected Point getGridPoint(Rectangle first, int i, int columns) {
		return first.topLeft().add(new Point(first.width * (i % columns), first.height * (int) (i / (float) columns)));
	}

}
