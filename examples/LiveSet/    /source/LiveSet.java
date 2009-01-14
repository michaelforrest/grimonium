import processing.core.*; 
import processing.xml.*; 

import processing.opengl.*; 
import microkontrol.controls.*; 
import microkontrol.*; 
import oscP5.*; 
import netP5.*; 
import grimonium.*; 
import grimonium.gui.*; 

import java.applet.*; 
import java.awt.*; 
import java.awt.image.*; 
import java.awt.event.*; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class LiveSet extends PApplet {













PFont font;
Grimonium grimonium;
GrimoniumView view;
public void setup() {
  size(1870, 380, OPENGL);
  hint(ENABLE_OPENGL_4X_SMOOTH) ;
  frameRate(30);
  load();
  font = loadFont("HelveticaNeue-CondensedBlack-20.vlw");
}
public void load(){
  grimonium = new Grimonium(this,"config.xml","mappings.xml");
  view = new GrimoniumView(this, grimonium);
}
public void draw(){

}  
public void keyPressed(){
  if(key  == UP) grimonium.set.previousSong();
  if(key == DOWN) grimonium.set.nextSong();
  if(key == 'R' ){
    view = null;
    grimonium = null;

    load();
  }
}
public void message(String m){
  textFont(font, 10);
  fill(0xFF00CC00);
  text(m, 0,10);
}


  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#c0c0c0", "LiveSet" });
  }
}
