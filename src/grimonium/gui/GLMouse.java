package grimonium.gui;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import processing.core.PApplet;
import processing.opengl.PGraphicsOpenGL;

public class GLMouse {
	private static GLMouse instance;
	private GL gl;
	private GLU glu;
	private final PApplet applet;

	GLMouse(PApplet applet){
		this.applet = applet;
		gl = ((PGraphicsOpenGL)applet.g).gl;
		glu = ((PGraphicsOpenGL)applet.g).glu;
	}
	public float[] getMouse3D()
	{
	  ((PGraphicsOpenGL) applet.g).beginGL();
	// have to get processing to dump all it's matricies into GL, so the
	// functions work.

	  int viewport[] = new int[4];
	// For the viewport matrix... not sure what all the values are, I think the
	// first two are width and height, and all Matricies in GL seem to be 4 or
	// 16...

	  double[] proj=new double[16];
	// For the Projection Matrix, 4x4

	  double[] model=new double[16];
	// For the Modelview Matrix, 4x4

	  gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);
	// fill the viewport matrix

	  gl.glGetDoublev(GL.GL_PROJECTION_MATRIX,proj,0);
	// projection matrix

	  gl.glGetDoublev(GL.GL_MODELVIEW_MATRIX,model,0);
	// modelview matrix

	  FloatBuffer fb=ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asFloatBuffer();
	// set up a floatbuffer to get the depth buffer value of the mouse position

	  gl.glReadPixels(applet.mouseX, applet.height-applet.mouseY, 1, 1, GL.GL_DEPTH_COMPONENT, GL.GL_FLOAT, fb);
	// Get the depth buffer value at the mouse position. have to do
	// height-mouseY, as GL puts 0,0 in the bottom left, not top left.

	  fb.rewind(); // finish setting up this.

	  double[] mousePosArr=new double[4];
	// the result x,y,z will be put in this.. 4th value will be 1, but I think
	// it's "scale" in GL terms, but I think it'll always be 1.

	 glu.gluUnProject((double)applet.mouseX,applet.height-(double)applet.mouseY,(double)fb.get(0), model,0,proj,0,viewport,0,mousePosArr,0);
	// the magic function. You put all the values in, and magically the x,y,z
	// values come out :)

	  ((PGraphicsOpenGL)applet.g).endGL();
	  return new float[]{(float)mousePosArr[0],(float)mousePosArr[1],(float)mousePosArr[2]};
	// The values are all doubles, so throw them into floats to make life
	// easier.
	}
	public static float[] pos(PApplet applet) {
		if(instance == null) instance = new GLMouse(applet);
		return instance.getMouse3D();
	}
}
