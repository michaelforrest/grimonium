import microkontrol.controls.*;
import microkontrol.*;

import oscP5.*;
import netP5.*;

import grimonium.*;
import grimonium.gui.*;
import rwmidi.*;

Grimonium grimonium;

void setup() {
  size(1860, 380);
  grimonium = new Grimonium(this,"config.xml","mappings.xml");

}
void draw(){
}
