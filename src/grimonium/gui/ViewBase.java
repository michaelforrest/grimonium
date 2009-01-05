package grimonium.gui;

import javax.media.opengl.GL;

import processing.core.PApplet;
import processing.opengl.PGraphicsOpenGL;

public class ViewBase {

	protected final PApplet applet;

	public ViewBase(PApplet applet) {
		this.applet = applet;

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

}
