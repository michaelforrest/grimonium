import rwmidi.*;
import microkontrol.controls.*;
import microkontrol.*;

MidiInput input;
MidiOutput output;
void setup() {
  size(800, 600);
  frameRate(30);
  background(0);
  println(RWMidi.getInputDevices());
  println(RWMidi.getOutputDevices());
  output = RWMidi.getOutputDevice("IAC Bus 3 <MOut:2> Apple Computer, Inc.").createOutput();
  input = RWMidi.getInputDevice("IAC Bus 3 <MIn:2> Apple Computer, Inc.").createInput(this);
}

void draw(){
}  
void keyPressed(){
   output.sendController(int(key)-48, 0, 0);
}

void sysexReceived(SysexMessage message){
  byte[] m = message.getMessage();
  println(message);
}

