import microkontrol.controls.*;
import microkontrol.*;

import ddf.minim.signals.*;
import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.effects.*;

import oscP5.*;
import netP5.*;

import grimonium.*;
import rwmidi.*;
Grimonium grimonium;
void setup() {
  size(400,400);
  grimonium = new Grimonium(this,"config.xml","mappings.xml");
}

void draw() {
}
