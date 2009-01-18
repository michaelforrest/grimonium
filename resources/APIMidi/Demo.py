import Live
from consts import *
from Logger import Logger
from Protocol import Dispatcher

class Demo:
    __module__ = __name__
    __doc__ = 'API Midi Demo \n'

    def __init__(self, c_instance, stdout):
        c_instance.show_message("APIMidi Demo")
        self._Demo__c_instance = c_instance
        self.logger = Logger(stdout)
        self.logger.log("API Midi Demo connected")
        # Dispatcher handles routing incoming requests (note / cc)
        self.dispatcher = Dispatcher(self,self.logger)
        
    def song(self):
        """returns a reference to the Live song instance that we control"""
        return self._Demo__c_instance.song()
                
    def connect_script_instances(self,scripts):
        pass
    
    def disconnect(self):
        self.logger.close()
        self.send_midi(GOODBYE_SYSEX_MESSAGE)
    
    def script_handle(self):
        return self._Demo__c_instance.handle()
        
    def update_display(self):
        pass
    
    def application(self):
        return Live.Application.get_application()
        
    def suggest_input_port(self):
        return ''
        
    def suggest_output_port(self):
        return ''
        
    def can_lock_to_devices(self):
        return True
        
    def suggest_map_mode(self,cc_no):
        return Live.MidiMap.MapMode.absolute
        
    def show_message(self, message):
        self._Demo__c_instance.show_message(message)
        
    def send_midi(self, midi_event_bytes):
        self._Demo__c_instance.send_midi(midi_event_bytes)
    
    def refresh_state(self):
        self.send_midi(WELCOME_SYSEX_MESSAGE)
    
    def build_midi_map(self, midi_map_handle):
        if DEBUG:
            self.logger.log("Build Midi Map")
        map_mode = Live.MidiMap.MapMode.absolute
        self.dispatcher.build_midi_map(self.script_handle(),midi_map_handle)
        self.send_midi(BUILD_MAP_SYSEX_MESSAGE)
        
    def receive_midi(self,midi_bytes):
        status = midi_bytes[0] & 240
        channel = midi_bytes[0] & 15
        #self.logger.log("MIDI IN " + str(status) + " : " + str(channel))
        if((status == NOTE_ON_STATUS) or (status == NOTE_OFF_STATUS)):
            note = midi_bytes[1]
            velocity = midi_bytes[2]
            self.dispatcher.handle_note(channel,note,velocity)
        elif (status == CC_STATUS):
            cc_no = midi_bytes[1]
            cc_value = midi_bytes[2]
            self.dispatcher.handle_cc(channel,cc_no,cc_value)

    def instance_identifier(self):
        return self._Demo__c_instance.instance_identifier()
            
    def sendSysex(self,data):
        """
        Data must be a tuple of bytes, remember only 7-bit data is allowed for sysex
        """
        self.send_midi(SYSEX_BEGIN + data + SYSEX_END)