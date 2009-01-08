import processing.opengl.*;

import microkontrol.controls.*;
import microkontrol.*;

import oscP5.*;
import netP5.*;

import grimonium.*;
import grimonium.gui.*;
import rwmidi.*;


PFont font;
Grimonium grimonium;
GrimoniumView view;
void setup() {
  size(1870, 380, OPENGL);
  hint(ENABLE_OPENGL_4X_SMOOTH) ;
  frameRate(30);
  load();
  font = loadFont("HelveticaNeue-CondensedBlack-20.vlw");
}
void load(){
  grimonium = new Grimonium(this,"config.xml","mappings.xml");
  view = new GrimoniumView(this, grimonium);
}
void draw(){

}  
void keyPressed(){
  if(key == 'R' ){
    view = null;
    grimonium = null;
 
    load();
  }
}
void message(String m){
   textFont(font, 10);
   fill(0xFF00CC00);
   text(m, 0,10);
}
