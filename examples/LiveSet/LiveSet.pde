import processing.opengl.*;

import microkontrol.controls.*;
import microkontrol.*;

import oscP5.*;
import netP5.*;

import grimonium.*;
import grimonium.gui.*;
import rwmidi.*;

Grimonium grimonium;

void setup() {
  size(1870, 380, OPENGL);
  hint(ENABLE_OPENGL_4X_SMOOTH) ;
  frameRate(30);
  grimonium = new Grimonium(this,"config.xml","mappings.xml");
  new GrimoniumView(this, grimonium);
}

void draw(){
}  

