import rwmidi.*;

import processing.opengl.*;

import microkontrol.controls.*;
import microkontrol.*;

import oscP5.*;
import netP5.*;

import grimonium.*;
import grimonium.gui.*;


PFont font;
Grimonium grimonium;
GrimoniumView view;
boolean isSetUp;
void setup() {
  size(1870, 380, OPENGL);
  frameRate(30);
  background(0);
 
  
  font = loadFont("app/HelveticaNeue-CondensedBlack-20.vlw");
   image(loadImage("app/cake.png"),0,0);
   textFont(font, 24);
   text("Starting up...",40 ,340);
}
void load(){
  
  grimonium = new Grimonium(this,"config.xml","mappings.xml");
  view = new GrimoniumView(this, grimonium);
  grimonium.set.previousSong();
}
void draw(){
  if(!isSetUp) {
    load();
    isSetUp = true;
  }
}  

