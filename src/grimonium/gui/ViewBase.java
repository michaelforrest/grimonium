package grimonium.gui;

import javax.media.opengl.GL;

import processing.core.PApplet;
import processing.opengl.PGraphicsOpenGL;

public class ViewBase {

	protected final PApplet applet;

	public ViewBase(PApplet applet) {
		this.applet = applet;

	}

	public void multiply() {
      PGraphicsOpenGL pGraphicsOpenGL = (PGraphicsOpenGL) applet.g;
      GL gl = pGraphicsOpenGL.beginGL();
      gl.glEnable(GL.GL_BLEND);
      gl.glEnable(GL.GL_TEXTURE_2D);
      gl.glBlendFunc(GL.GL_DST_COLOR, GL.GL_ZERO);
      pGraphicsOpenGL.endGL();
	}

	public void noBlend() {
		GL gl = ((PGraphicsOpenGL) applet.g).beginGL();
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		//gl.glDisable(GL.GL_BLEND);
		((PGraphicsOpenGL) applet.g).endGL();
	}

}
