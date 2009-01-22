import oscP5.*;
import netP5.*;

import grimonium.maps.*;
import grimonium.gui.*;
import rwmidi.*;
import grimonium.*;
import test.*;
import grimonium.set.*;

import microkontrol.controls.*;
import rwmidi.*;
import microkontrol.*;
MicroKontrol mk;

void setup(){
  Grimonium.boot(this);
  new FaderBone(0,"spanner").activate();
  new FaderBone(1,"gabbakick").activate();
  new FaderBone(2,"chinahat").activate();
}
void draw(){
}

