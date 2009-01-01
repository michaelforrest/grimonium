import ddf.minim.signals.*;
import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.effects.*;

import rwmidi.*;

import microkontrol.controls.*;
import microkontrol.*;

import oscP5.*;
import netP5.*;

import grimonium.*;
Grimonium grimonium;
void setup(){
  grimonium = new Grimonium(this, "config.xml", "mappings.xml"); 
}
