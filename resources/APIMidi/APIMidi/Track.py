from consts import *
import Utils
import Live

class TrackDataHandler:
    """
    Handles request for track data on the assigned channel via midi cc messages
    """
    def __init__(self,channel,controller,logger):
        self.controller = controller
        self.logger = logger
        self.channel = channel
        self.builder = TrackSysexBuilder()
        if DEBUG:
            self.logger.log("TrackDataHandler " + str(self.channel))
    
    def build_midi_map(self,script_handle,midi_map_handle):
        map_mode = Live.MidiMap.MapMode.absolute
        Live.MidiMap.forward_midi_cc(script_handle,midi_map_handle,self.channel,TRACK_DATA_CC)
    
    def handle_cc(self,chan,num,val):
        """
        Only need to use the value of the cc which is the track index.
        """
        self.logger.log("TDH " + str(num) + "," + str(val))
        return self.builder.getTrackData(val)
    
    def can_handle(self,chan,num,val):
        return chan == self.channel and num == TRACK_DATA_CC
        
class TrackMonitorHandler:
    """
    Handles requests for listening to track attributes
    """
    def __init__(self, channel, controller,logger):
        self.controller = controller
        self.logger = logger
        self.channel = channel
        self.monitors = []
        self.setSize(MAX_TRACKS,MAX_TRACKS)
        if DEBUG:
            self.logger.log("TrackMonitorHandler " + str(self.channel))
        
    def setSize(self,numTracks,numScenes):
        self.numTracks = numTracks
        
        if len(self.monitors) > 0:
            self.clearMonitors()
            
        self.monitors = []
        # Create storage for our listeners
        for index in range(self.numTracks):
            self.monitors.append(None)
    
    def setSceneOffset(self):
        pass
    
    def clearMonitors(self):
        for index in range(len(self.monitors)):
            monitor = self.monitors[index]
            monitor.stop()
            
    def build_midi_map(self,script_handle,midi_map_handle):
        map_mode = Live.MidiMap.MapMode.absolute
        Live.MidiMap.forward_midi_cc(script_handle,midi_map_handle,self.channel,TRACK_MONITOR_CC)
        Live.MidiMap.forward_midi_cc(script_handle,midi_map_handle,self.channel,TRACK_STOP_MONITOR_CC)

    def handle_cc(self,chan,num,val):
        if val < len(self.monitors):
            trackMonitor = self.monitors[val]
        else:
            if DEBUG:
                self.logger.log("Track out of range : " + str(val) + " : " + str(len(self.monitors)))
            return

        # If the monitor doesn't exist, create it
        if not trackMonitor:
            if DEBUG:
                self.logger.log("Creating track monitor at " + str(val))
            trackMonitor = TrackMonitor(self.controller,self.logger)
            
            self.monitors[val] = trackMonitor

        if num == TRACK_MONITOR_CC:
            if trackMonitor.isActive():
                trackMonitor.stop()
            trackMonitor.start(val)
        elif num == TRACK_STOP_MONITOR_CC:
            trackMonitor.stop()

    def can_handle(self,chan,num,val):
        canHandleCC = (num == TRACK_MONITOR_CC) or (num == TRACK_STOP_MONITOR_CC)
        return chan == self.channel and canHandleCC

class TrackMonitor:
    """
    Monitors a single track for changes.
    """
    def __init__(self,controller,logger):
        self.controller = controller
        self.logger = logger
        if DEBUG:
            self.logger.log("New TrackMonitor")
        self.builder = TrackSysexBuilder()
        self.active = 0
        
    def start(self,trackIndex):
        self.trackIndex = trackIndex
        self.track = Utils.getTrack(self.trackIndex)
        
        if self.track:
            if DEBUG:
                self.logger.log("Monitoring Track " + str(self.trackIndex))
            self.listenToTrack()
            self.active = 1

    def stop(self):
        if DEBUG:
            self.logger.log("Stop Monitoring Track " + str(self.trackIndex))
        self.stopListeningToTrack()
        self.active = 0
                   
    def listenToTrack(self):
        if self.track:
            if self.track.arm_has_listener(self.armChanged):
                if DEBUG:
                    self.logger.log("Track already has listener"  + str(self.trackIndex) + " : " + self.track.name)
                self.stopListeningToTrack()
                
            if DEBUG:
                self.logger.log("Listening to track " + str(self.trackIndex) + " : " + self.track.name)
            self.track.add_arm_listener(self.armChanged)
            self.track.add_mute_listener(self.muteChanged)
            self.track.add_solo_listener(self.soloChanged)
            self.track.add_name_listener(self.nameChanged)
            self.trackChanged()
        else:
            if DEBUG:
                self.logger.log("No track to monitor " + str(self.trackIndex))
        
    def stopListeningToTrack(self):
        if self.track:
            if DEBUG:
                self.logger.log("Stopping listening to track " + str(self.trackIndex) + " : " + self.track.name)
            self.track.remove_arm_listener(self.armChanged)
            self.track.remove_mute_listener(self.muteChanged)
            self.track.remove_solo_listener(self.soloChanged)
            self.track.remove_name_listener(self.nameChanged)

    def isActive(self):
        return self.active
    
    def trackChanged(self):
        """
        Generic track change handler
        """
        if DEBUG:
            self.logger.log("Track changed " + str(self.trackIndex))
        self.controller.sendSysex(self.builder.getTrackData(self.trackIndex))
    
    def nameChanged(self):
        if DEBUG:
            self.logger.log("Track name changed : " + self.track.name)
        data = TRACK_NAME + (self.trackIndex,) + Utils.translateString(self.track.name)
        self.controller.sendSysex(data)
        
    def armChanged(self):
        if DEBUG:
            self.logger.log("Track arm changed : " + str(self.track.arm))
        data = TRACK_IS_ARMED + (self.trackIndex,self.track.arm)
        self.controller.sendSysex(data)
        
    def muteChanged(self):
        if DEBUG:
            self.logger.log("Track mute changed : " + str(self.track.mute))
        data = TRACK_IS_MUTED + (self.trackIndex,self.track.mute)
        self.controller.sendSysex(data)
        
    def soloChanged(self):
        if DEBUG:
            self.logger.log("Track solo changed : " + str(self.track.solo))
        data = TRACK_IS_SOLOED + (self.trackIndex,self.track.solo)
        self.controller.sendSysex(data)
        
class TrackSysexBuilder:
    def getTrackData(self,trackIndex):
        track = Utils.getTrack(trackIndex)
        
        if track:
            return self.buildTrackData(trackIndex,track)
        else:
            return NO_TRACK + (trackIndex,)
        
    def buildTrackData(self,index,track):
        nameData = Utils.translateString(track.name)
        
        flags = track.arm << 3
        flags += track.can_be_armed << 2
        flags += track.mute << 1
        flags += track.solo
    
        data = TRACK_DATA + (index,flags) + nameData
        return data
    