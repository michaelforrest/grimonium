from consts import *
import Utils
import Live
from Clip import *
from Track import *
from Scene import *
from Song import *
from CuePoint import *

class Dispatcher:
    """
    General request dispatcher.
    """
    def __init__(self,controller,logger):
        self.controller = controller
        self.logger = logger
        self.handlers = {}

        clipDataHandler = ClipDataHandler(CLIP_DATA_CHANNEL, self.controller,self.logger)
        clipTriggerHandler = ClipTriggerHandler(CLIP_TRIGGER_CHANNEL, self.controller, self.logger)
        clipMonitorHandler = ClipMonitorHandler(CLIP_MONITOR_CHANNEL, CLIP_STOP_MONITOR_CHANNEL, self.controller,self.logger)

        self.handlers[CLIP_TRIGGER_CHANNEL] = clipTriggerHandler
        self.handlers[CLIP_DATA_CHANNEL] = clipDataHandler
        self.handlers[CLIP_MONITOR_CHANNEL] = clipMonitorHandler
        self.handlers[CLIP_STOP_MONITOR_CHANNEL] = clipMonitorHandler
        
        sceneDataHandler = SceneDataHandler(SCENE_DATA_CHANNEL, self.controller, self.logger)
        sceneTriggerHandler = SceneTriggerHandler(SCENE_TRIGGER_CHANNEL, self.controller, self.logger)
        sceneMonitorHandler = SceneMonitorHandler(SCENE_MONITOR_CHANNEL, SCENE_STOP_MONITOR_CHANNEL, self.controller, self.logger)

        self.handlers[SCENE_TRIGGER_CHANNEL] = sceneTriggerHandler
        self.handlers[SCENE_DATA_CHANNEL] = sceneDataHandler
        self.handlers[SCENE_MONITOR_CHANNEL] = sceneMonitorHandler
        self.handlers[SCENE_STOP_MONITOR_CHANNEL] = sceneMonitorHandler

        trackDataHandler = TrackDataHandler(GENERAL_REQUEST_CHANNEL, self.controller, self.logger)
        trackMonitorHandler = TrackMonitorHandler(GENERAL_REQUEST_CHANNEL, self.controller, self.logger)
        
        cuePointDataHandler = CuePointDataHandler(GENERAL_REQUEST_CHANNEL, self.controller, self.logger)
        cuePointMonitorHandler = CuePointMonitorHandler(GENERAL_REQUEST_CHANNEL, self.controller, self.logger)

        sizeHandlers = [sceneDataHandler,sceneTriggerHandler,sceneMonitorHandler,trackMonitorHandler,clipMonitorHandler,clipDataHandler,clipTriggerHandler]
        sizeController = SizeController(GENERAL_REQUEST_CHANNEL,self.controller,self.logger,sizeHandlers)
        
        songDataHandler = SongDataHandler(GENERAL_REQUEST_CHANNEL, self.controller,self.logger)
        
        self.handlers[GENERAL_REQUEST_CHANNEL] = [trackDataHandler,trackMonitorHandler,sizeController,songDataHandler]
        
    def build_midi_map(self,script_handle,midi_map_handle):
        for channel, handler in self.handlers.iteritems():
            if isinstance(handler,list):
                for subhandler in handler:
                    subhandler.build_midi_map(script_handle,midi_map_handle)
            else:
                handler.build_midi_map(script_handle,midi_map_handle)
    
    def handle_note(chan,note,velocity):
        if DEBUG:
            self.logger.log("Note " + str(chan) + " : " + str(note) + " : " + note(velocity))
        
    def handle_cc(self,chan,num,val):
        if DEBUG:
            self.logger.log("CC " + str(chan) + " : " + str(num) + " : " + str(val))

        handler = self.handlers[chan]
        if handler:
            if isinstance(handler,list):
                for subhandler in handler:
                    if subhandler.can_handle(chan,num,val):
                        data = subhandler.handle_cc(chan,num,val)
                        break
            else:
                data = handler.handle_cc(chan,num,val)
            if data:
                self.controller.sendSysex(data)
        else:
            strTuple = self.translate_string("Unknown")
            self.controller.sendSysex(strTuple)

class SizeController:
    def __init__(self,channel, controller, logger, handlers):
        self.channel = channel
        self.controller = controller
        self.logger = logger
        self.handlers = handlers
        if DEBUG:
            self.logger.log("SizeController " + str(self.channel))
        
    def setSize(self,tracks,scenes):
        if DEBUG:
            self.logger.log("Set Size " + str(tracks) + ", " + str(scenes))
        
        self.numTracks = tracks
        self.numScenes = scenes

        for handler in handlers:
            handler.setSize(tracks,scenes)

        data = NUM_TRACKS + Utils.intToMidi(self.numTracks)
        self.controller.sendSysex(data)
        
    def handle_cc(self,chan,num,val):
        
        if val > 63:
            val = 127
        elif val > 31:
            val = 63
        elif val > 15:
            val = 31
        elif val > 7:
            val = 15
        elif val > 3:
            val = 7
        else:
            val = 3
            
        tracks = val
        scenes = (16384 / (val + 1)) - 1
        setSize(tracks,scenes)

    def handle_note(self,chan,num,val):
        """
        Note number and velocity are treated as a 14-bit number to set
        a global scene offset
        """
        sceneOffset = (num << 7) + val
        for handler in handlers:
            try:
                handler.setSceneOffset(sceneOffset)
            except:
                if DEBUG:
                    self.logger.log("Couldnt set scene offset")
            
    def build_midi_map(self,script_handle,midi_map_handle):
        map_mode = Live.MidiMap.MapMode.absolute
        Live.MidiMap.forward_midi_cc(script_handle,midi_map_handle,self.channel,NUM_TRACKS_CC)
        Utils.mapAllNotes(script_handle, midi_map_handle, self.channel)
            
    def can_handle(self,chan,num,val):
        return chan == self.channel and num == NUM_TRACKS_CC        