from consts import *
import Utils
import Live

class ClipDataHandler:
    """
    Handles requests for clip data on the assigned channel via midi cc messages.
    CC number is track and CC value is scene
    """
    def __init__(self, channel, controller, logger):
        self.controller = controller
        self.logger = logger
        self.channel = channel
        self.builder = ClipSysexBuilder(logger)
        self.tracks = MAX_TRACKS
        self.scenes = MAX_TRACKS
        if DEBUG:
            self.logger.log("ClipDataHandler " + str(self.channel))
        
    def build_midi_map(self,script_handle,midi_map_handle):
        map_mode = Live.MidiMap.MapMode.absolute
        Utils.mapAllCCs(script_handle,midi_map_handle,self.channel)
    
    def handle_cc(self,chan,num,val):
        if self.tracks == MAX_TRACKS:
            x = num
            y = val
        else:
            address = (num << 7) + val
            x = floor(address / self.tracks)
            y = address % tracks
            
        if DEBUG:
            self.logger.log("CDH " + str(x) + "," + str(y))
            
        return self.builder.getClipData(x,y)

    def setSize(self,tracks,scenes):
        self.tracks = tracks
        
class ClipTriggerHandler:
    """
    Handles requests to fire clips
    """
    def __init__(self, channel, controller, logger):
        self.controller = controller
        self.logger = logger
        self.channel = channel
        self.tracks = MAX_TRACKS
        if DEBUG:
            self.logger.log("ClipTriggerHandler " + str(self.channel))
        
    def build_midi_map(self,script_handle,midi_map_handle):
        map_mode = Live.MidiMap.MapMode.absolute
        Utils.mapAllCCs(script_handle,midi_map_handle,self.channel)
    
    def handle_cc(self,chan,num,val):
        if DEBUG:
            self.logger.log("Trigger " + str(num) + "," + str(val))
        
        if self.tracks == MAX_TRACKS:
            x = num
            y = val
        else:
            address = (num << 7) + val
            x = floor(address / self.tracks)
            y = address % tracks
                    
        clipSlot = Utils.getClipSlot(num, val)
        if clipSlot:
            clipSlot.fire()
    
    def setSize(self,tracks,scenes):
        self.tracks = tracks
        
class ClipMonitorHandler:
    """
    Handles requests for listening to clip attributes
    """
    def __init__(self, monitorChannel, stopMonitorChannel, controller,logger):
        self.controller = controller
        self.logger = logger
        self.monitorChannel = monitorChannel
        self.stopMonitorChannel = stopMonitorChannel
        self.monitors = []
        self.setSize(MAX_TRACKS, MAX_TRACKS)
        if DEBUG:
            self.logger.log("ClipMonitorHandler " + str(self.monitorChannel) + " : " + str(self.stopMonitorChannel))
        
    def setSize(self,tracks,scenes):
        self.tracks = tracks;
        self.scenes = scenes;
        
        if len(self.monitors) > 0:
            self.clearMonitors()
            
        self.monitors = []
        # Create storage for our listeners
        for index in range(self.tracks):
            clipList = []
            for clipIndex in range(self.scenes):
                clipList.append(None)
            self.monitors.append(clipList)
        
    def clearMonitors(self):
        for index in range(len(self.monitors)):
            clipList = self.monitors[index]
            for clipIndex in range(len(clipList)):
                monitor = clipList[clipIndex]
                monitor.stop()
        
    def build_midi_map(self,script_handle,midi_map_handle):
        map_mode = Live.MidiMap.MapMode.absolute
        Utils.mapAllCCs(script_handle,midi_map_handle,self.monitorChannel)
        Utils.mapAllCCs(script_handle,midi_map_handle,self.stopMonitorChannel)

    def handle_cc(self,chan,num,val):
        
        if self.tracks == MAX_TRACKS:
            x = num
            y = val
        else:
            address = (num << 7) + val
            x = floor(address / self.tracks)
            y = address % tracks
                    
        clipMonitor = self.monitors[x][y]

        
        # If the monitor doesn't exist, create it
        if not clipMonitor:
            if DEBUG:
                self.logger.log("Creating monitor at " + str(x) + "," + str(y))
            clipMonitor = ClipMonitor(self.controller,self.logger)
            self.monitors[x][y] = clipMonitor

        if chan == self.monitorChannel:
            if clipMonitor.isActive():
                clipMonitor.stop()
            clipMonitor.start(x,y)
        elif chan == self.stopMonitorChannel:
            clipMonitor.stop()

class ClipMonitor:
    """
    Monitors a single clip slot and its clip for changes.
    If a new clip is added to the clip slot and the monitor is active 
    then that clip is automatically monitored
    """
    def __init__(self,controller,logger):
        self.controller = controller
        self.logger = logger
        if DEBUG:
            self.logger.log("New ClipMonitor")
        self.builder = ClipSysexBuilder(logger)
        self.active = 0
        
    def start(self,x,y):
        self.x = x
        self.y = y
        self.clipSlot = Utils.getClipSlot(x, y)
        self.positionData = self.builder.getPositionData(self.x, self.y)
        
        if self.clipSlot:
            if DEBUG:
                self.logger.log("Monitoring ClipSlot " + str(self.x) + "," + str(self.y))
            if not self.clipSlot.has_clip_has_listener(self.hasClipChanged):
                self.clipSlot.add_has_clip_listener(self.hasClipChanged)
                self.clip = self.clipSlot.clip
                self.listenToClip()
                self.active = 1

    def stop(self):
        if DEBUG:
            self.logger.log("Stop Monitoring ClipSlot " + str(self.x) + "," + str(self.y))
        self.stopListeningToClip()
        self.clipSlot.remove_has_clip_listener(self.hasClipChanged)
        self.active = 0
                   
    def listenToClip(self):
        if self.clip:
            if self.clip.is_playing_has_listener(self.playingChanged):
                if DEBUG:
                    self.logger.log("Clip already has listener "  + str(self.x) + "," + str(self.y) + " : " + self.clip.name)
                
                # Clear existing listeners
                self.stopListeningToClip()
            
            if DEBUG:
                self.logger.log("Listening to clip " + str(self.x) + "," + str(self.y) + " : " + self.clip.name)
                
            self.clip.add_is_playing_listener(self.playingChanged)
            self.clip.add_is_triggered_listener(self.triggeredChanged)
            self.clip.add_looping_listener(self.loopingChanged)
            self.clip.add_name_listener(self.nameChanged)
            self.clip.add_color_index_listener(self.colorChanged)
            self.clipChanged()
        else:
            if DEBUG:
                self.logger.log("No clip to monitor " + str(self.x) + "," + str(self.y))
        
    def stopListeningToClip(self):
        if self.clip:
            if DEBUG:
                self.logger.log("Stopping listening to clip " + str(self.x) + "," + str(self.y) + " : " + self.clip.name)
            self.clip.remove_is_playing_listener(self.playingChanged)
            self.clip.remove_is_triggered_listener(self.triggeredChanged)
            self.clip.remove_looping_listener(self.loopingChanged)
            self.clip.remove_name_listener(self.nameChanged)
            self.clip.remove_color_index_listener(self.colorChanged)

    def isActive(self):
        return self.active
    
    def hasClipChanged(self):
        if DEBUG:
            self.logger.log("HasClip Changed " + str(self.x) + "," + str(self.y) + " : " + str(self.clipSlot.has_clip))
        # Stop listening to the old clip
        if self.clip:
            self.stop()

        if(self.clipSlot.has_clip):
            # If there is a new one start listening and send change notification
            self.clip = self.clipSlot.clip
            self.listenToClip()
            self.clipChanged()
        else:
            # No clip, so send an empty clip slot message
            self.controller.sendSysex(EMPTY_CLIP_SLOT + self.positionData)
            
    def clipChanged(self):
        """
        Generic clip change handler
        """
        if DEBUG:
            self.logger.log("Clip changed " + str(self.x) + "," + str(self.y) + " : " + self.clip.name)
        self.controller.sendSysex(self.builder.getClipData(self.x,self.y))
        
    def triggeredChanged(self):
        """
        NOTE: The value of clip.is_triggered isnt correct at the time this handler fires. Its always zero
        So, when handling the 'is_triggered' clip attribute in your application just remember 
        ONLY ONE clip per track can be in a triggered state
        """
        if DEBUG:
            self.logger.log("Clip is_triggered change : " + str(self.x) + "," + str(self.y) + " : " + str(self.clip.is_triggered))
        data = CLIP_IS_TRIGGERED + self.positionData + (1,)
        self.controller.sendSysex(data)
    
    def nameChanged(self):
        if DEBUG:
            self.logger.log("Clip name changed : " + self.clip.name)
        data = CLIP_NAME + self.positionData + Utils.translateString(self.clip.name)
        self.controller.sendSysex(data)
        
    def loopingChanged(self):
        if DEBUG:
            self.logger.log("Clip looping changed : " + str(self.clip.looping))
        data = CLIP_IS_LOOPING + self.positionData + (self.clip.looping,)
        self.controller.sendSysex(data)
        
    def playingChanged(self):
        if DEBUG:
            self.logger.log("Clip playing changed : " + str(self.x) + "," + str(self.y) + " : " + str(self.clip.is_playing))
        data = CLIP_IS_PLAYING + self.positionData + (self.clip.is_playing,)
        self.controller.sendSysex(data)

    def colorChanged(self):
        if DEBUG:
            self.logger.log("Clip color changed : " + str(self.x) + "," + str(self.y) + " : " + str(self.clip.color_index))
        data = CLIP_COLOR + self.positionData + (self.clip.color_index,)
        self.controller.sendSysex(data)
        
class ClipSysexBuilder:
    def __init__(self,logger):
        """
        """
        self.logger = logger
        
    def getPositionData(self,trackIndex,sceneIndex):
        return (trackIndex,) + Utils.intToMidi(sceneIndex)
    
    def getClipData(self,trackIndex,sceneIndex):
        clip = Utils.getClip(trackIndex, sceneIndex)
        if clip:
            return self.buildClipData(trackIndex,sceneIndex,clip)
        else:
            return EMPTY_CLIP_SLOT + self.getPositionData(trackIndex, sceneIndex)

    def buildClipData(self,trackIndex,sceneIndex,clip):
        nameData = Utils.translateString(clip.name)
        
        # Build a single byte for the boolean flags, 
        # is_audio_clip and is_midi_clip are mutually exclusive so only need the one bit for both of them
        flags = clip.muted << 5

        if clip.is_audio_clip:
            flags += clip.warping << 4
        
        flags += clip.looping << 3
        flags += clip.is_playing << 2
        flags += clip.is_triggered << 1
        flags += clip.is_audio_clip
        
        pos = self.getPositionData(trackIndex,sceneIndex)
        
        data = CLIP_DATA + self.getPositionData(trackIndex, sceneIndex) + (clip.color_index,flags) + nameData
        return data