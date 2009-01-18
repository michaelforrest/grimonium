from consts import *
import Utils
import Live

class CuePointDataHandler:
    """
    Handles request for cue point data on the assigned channel via midi cc messages
    """
    def __init__(self,channel,controller,logger):
        self.controller = controller
        self.logger = logger
        self.channel = channel
        self.builder = CuePointSysexBuilder()
        if DEBUG:
            self.logger.log("CuePointDataHandler " + str(self.channel))
    
    def build_midi_map(self,script_handle,midi_map_handle):
        map_mode = Live.MidiMap.MapMode.absolute
        Live.MidiMap.forward_midi_cc(script_handle,midi_map_handle,self.channel,CUEPOINT_DATA_CC)
        Live.MidiMap.forward_midi_cc(script_handle,midi_map_handle,self.channel,CUEPOINT_FIRE_CC)
    
    def handle_cc(self,chan,num,val):
        """
        Only need to use the value of the cc which is the cuepoint index.
        """
        if DEBUG:
            self.logger.log("CPDH " + str(num) + "," + str(val))
        return self.builder.getCuePointData(val)
    
    def can_handle(self,chan,num,val):
        handle_cc = num == CUEPOINT_DATA_CC or num == CUEPOINT_FIRE_CC
        return chan == self.channel and handle_cc
        
class CuePointMonitorHandler:
    """
    Handles requests for listening to cue point attributes
    """
    def __init__(self, channel, controller,logger):
        self.controller = controller
        self.logger = logger
        self.channel = channel
        self.monitors = []
        self.setSize(127)
        if DEBUG:
            self.logger.log("CuePointMonitorHandler " + str(self.channel))
        
    def setSize(self,numCuePoints):
        self.numCuePoints = numCuePoints
        
        if len(self.monitors) > 0:
            self.clearMonitors()
            
        self.monitors = []
        # Create storage for our listeners
        for index in range(self.numCuePoints):
            self.monitors.append(None)
    
    def setSceneOffset(self):
        pass
    
    def clearMonitors(self):
        for index in range(len(self.monitors)):
            monitor = self.monitors[index]
            monitor.stop()
            
    def build_midi_map(self,script_handle,midi_map_handle):
        map_mode = Live.MidiMap.MapMode.absolute
        Live.MidiMap.forward_midi_cc(script_handle,midi_map_handle,self.channel,CUEPOINT_MONITOR_CC)
        Live.MidiMap.forward_midi_cc(script_handle,midi_map_handle,self.channel,CUEPOINT_STOP_MONITOR_CC)

    def handle_cc(self,chan,num,val):
        if val < len(self.monitors):
            monitor = self.monitors[val]
        else:
            if DEBUG:
                logger.log("CuePoint out of range : " + str(val) + " : " + str(len(self.monitors)))
            return

        # If the monitor doesn't exist, create it
        if not monitor:
            if DEBUG:
                self.logger.log("Creating cuePoint monitor at " + str(val))
            monitor = CuePointMonitor(self.controller,self.logger)
            
            self.monitors[val] = monitor

        if num == CUEPOINT_MONITOR_CC:
            if monitor.isActive():
                monitor.stop()
            monitor.start(val)
        elif num == CUEPOINT_STOP_MONITOR_CC:
            monitor.stop()

    def can_handle(self,chan,num,val):
        canHandleCC = (num == CUEPOINT_MONITOR_CC) or (num == CUEPOINT_STOP_MONITOR_CC)
        return chan == self.channel and canHandleCC

class CuePointMonitor:
    """
    Monitors a single cue point for changes.
    """
    def __init__(self,controller,logger):
        self.controller = controller
        self.logger = logger
        if DEBUG:
            self.logger.log("New CuePointMonitor")
        self.builder = CuePointSysexBuilder()
        self.active = 0
        
    def start(self,index):
        self.index = index
        self.cuePoint = Utils.getCuePoint(self.index)
        
        if self.cuePoint:
            if DEBUG:
                self.logger.log("Monitoring CuePoint " + str(self.index))
            self.listenToCuePoint()
            self.active = 1

    def stop(self):
        if DEBUG:
            self.logger.log("Stop Monitoring CuePoint " + str(self.index))
        self.stopListeningToCuePoint()
        self.active = 0
                   
    def listenToCuePoint(self):
        if self.cuePoint:
            if self.cuePoint.name_has_listener(self.nameChanged):
                if DEBUG:
                    self.logger.log("CuePoint already has listener "  + str(self.index) + " : " + self.cuePoint.name)
                self.stopListeningToCuePoint()
                    
            if DEBUG:
                self.logger.log("Listening to cuePoint " + str(self.index) + " : " + self.cuePoint.name)
            self.cuePoint.add_name_listener(self.nameChanged)
            self.cuePoint.add_time_listener(self.timeChanged)
            self.cuePointChanged()
        else:
            if DEBUG:
                self.logger.log("No cuePoint to monitor " + str(self.index))
        
    def stopListeningToCuePoint(self):
        if self.cuePoint:
            if DEBUG:
                self.logger.log("Stopping listening to cuePoint " + str(self.index) + " : " + self.cuePoint.name)
            self.cuePoint.remove_time_listener(self.timeChanged)
            self.cuePoint.remove_name_listener(self.nameChanged)

    def isActive(self):
        return self.active
    
    def cuePointChanged(self):
        """
        Generic cuePoint change handler
        """
        if DEBUG:
            self.logger.log("CuePoint changed " + str(self.index))
        self.controller.sendSysex(self.builder.getCuePointData(self.index))
    
    def nameChanged(self):
        if DEBUG:
            self.logger.log("CuePoint name changed : " + self.cuePoint.name)
        data = CUEPOINT_NAME + (self.index,) + Utils.translateString(self.cuePoint.name)
        self.controller.sendSysex(data)
        
    def armChanged(self):
        if DEBUG:
            self.logger.log("CuePoint time changed : " + str(self.cuePoint.time))
        data = CUEPOINT_TIME + (self.index,self.cuePoint.time)
        self.controller.sendSysex(data)

class CuePointSysexBuilder:
    def getCuePointData(self,index):
        cuePoint = Utils.getCuePoint(index)
        
        if cuePoint:
            return self.buildCuePointData(index,cuePoint)
        else:
            return NO_CUEPOINT + (cuePointIndex,)
        
    def buildCuePointData(self,index,cuePoint):
        nameData = Utils.translateString(cuePoint.name)
        
        data = CUEPOINT_DATA + (index,) + nameData
        return data
    