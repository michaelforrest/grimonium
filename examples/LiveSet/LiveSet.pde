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
  hint(ENABLE_OPENGL_4X_SMOOTH) ;
  frameRate(30);
  background(0);
 
  
  font = loadFont("HelveticaNeue-CondensedBlack-20.vlw");
   image(loadImage("cake.png"),0,0);
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
void keyPressed(){
  if(key  == UP) grimonium.set.previousSong();
  if(key == DOWN) grimonium.set.nextSong();
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

