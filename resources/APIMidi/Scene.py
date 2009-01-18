from consts import *
import Utils
import Live

class SceneDataHandler:
    """
    Handles request for scene data on the assigned channel via midi cc messages
    """
    def __init__(self,channel,controller,logger):
        self.controller = controller
        self.logger = logger
        self.channel = channel
        self.builder = SceneSysexBuilder()
        if DEBUG:
            self.logger.log("SceneDataHandler " + str(self.channel))

    def build_midi_map(self,script_handle,midi_map_handle):
        map_mode = Live.MidiMap.MapMode.absolute
        Utils.mapAllCCs(script_handle,midi_map_handle,self.channel)
            
    def handle_cc(self,chan,num,val):
        """
        Only need to use the value of the cc which is the scene index.
        """
        sceneIndex = (num << 7) + val
        if DEBUG:
            self.logger.log("SDH " + str(sceneIndex))
        return self.builder.getSceneData(sceneIndex)
    
    def setSceneOffset(self,sceneOffset):
        self.sceneOffset = sceneOffset
        
    def setSize(self,tracks,scenes):
        pass
    
class SceneTriggerHandler:
    """
    Handles requests to fire scenes
    """
    def __init__(self, channel, controller, logger):
        self.controller = controller
        self.logger = logger
        self.channel = channel
        if DEBUG:
            self.logger.log("SceneTriggerHandler " + str(self.channel))
    
    def build_midi_map(self,script_handle,midi_map_handle):
        map_mode = Live.MidiMap.MapMode.absolute
        Utils.mapAllCCs(script_handle,midi_map_handle,self.channel)
            
    def handle_cc(self,chan,num,val):
        sceneIndex = (num << 7) + val
        if DEBUG:
            self.logger.log("Trigger scene " + str(sceneIndex))
        scene = Utils.getScene(sceneIndex)
        if scene:
            scene.fire()

    def setSize(self,tracks,scenes):
        pass
    
    def setSceneOffset(self,sceneOffset):
        self.sceneOffset = sceneOffset
 
class SceneMonitorHandler:
    """
    Handles requests for listening to scene attributes
    """
    def __init__(self, monitorChannel, stopMonitorChannel, controller,logger):
        self.controller = controller
        self.logger = logger
        self.monitorChannel = monitorChannel
        self.stopMonitorChannel = stopMonitorChannel
        self.monitors = []
        self.setSize(MAX_TRACKS, MAX_TRACKS)
        if DEBUG:
            self.logger.log("SceneMonitorHandler " + str(self.monitorChannel) + " : " + str(self.stopMonitorChannel))
                    
    def setSize(self,tracks,scenes):
        self.numScenes = scenes
        
        if len(self.monitors) > 0:
            self.clearMonitors()
                    
        self.monitors = []
        # Create storage for our listeners
        for index in range(self.numScenes):
            self.monitors.append(None)

    def setSceneOffset(self,sceneOffset):
        self.sceneOffset = sceneOffset
        
    def clearMonitors(self):
        for index in range(len(self.monitors)):
            monitor = self.monitors[index]
            monitor.stop()
                    
    def build_midi_map(self,script_handle,midi_map_handle):
        map_mode = Live.MidiMap.MapMode.absolute
        Utils.mapAllCCs(script_handle,midi_map_handle,self.monitorChannel)
        Utils.mapAllCCs(script_handle,midi_map_handle,self.stopMonitorChannel)
        
    def handle_cc(self,chan,num,val):
        sceneIndex = (num << 7) + val
        sceneMonitor = self.monitors[sceneIndex]

        # If the monitor doesn't exist, create it
        if not sceneMonitor:
            if DEBUG:
                self.logger.log("Creating scene monitor at " + str(sceneIndex))
            sceneMonitor = SceneMonitor(self.controller,self.logger)
            self.monitors[sceneIndex] = sceneMonitor

        if chan == self.monitorChannel:
            if sceneMonitor.isActive():
                sceneMonitor.stop()
            sceneMonitor.start(sceneIndex)
        elif chan == self.stopMonitorChannel:
            sceneMonitor.stop()

class SceneMonitor:
    """
    Monitors a single scene for changes
    """
    def __init__(self,controller,logger):
        self.controller = controller
        self.logger = logger
        if DEBUG:
            self.logger.log("New SceneMonitor")
        self.builder = SceneSysexBuilder()
        self.active = 0
        
    def start(self,sceneIndex):
        self.sceneIndex = sceneIndex
        self.scene = Utils.getScene(self.sceneIndex)
        self.sceneName = self.scene.name
        
        if self.scene:
            if DEBUG:
                self.logger.log("Monitoring Scene " + str(self.sceneIndex))
            self.listenToScene()
            self.active = 1

    def stop(self):
        if DEBUG:
            self.logger.log("Stop Monitoring Scene " + str(self.sceneIndex))
        self.stopListeningToScene()
        self.active = 0
                   
    def listenToScene(self):
        if self.scene:
            if self.scene.name_has_listener(self.nameChanged):
                if DEBUG:
                    self.logger.log("Scene already has listener "  + str(self.sceneIndex) + " : " + self.scene.name)
                self.stopListeningToScene()
                
            if DEBUG:
                self.logger.log("Listening to scene " + str(self.sceneIndex) + " : " + self.scene.name)
            self.scene.add_name_listener(self.nameChanged)
            self.sceneChanged()
        else:
            self.logger.log("No scene to monitor " + str(self.sceneIndex))
        
    def stopListeningToScene(self):
        if self.scene:
            if DEBUG:
                self.logger.log("Stopping listening to scene " + str(self.sceneIndex) + " : " + self.scene.name)
            self.scene.remove_name_listener(self.nameChanged)

    def isActive(self):
        return self.active
    
    def sceneChanged(self):
        """
        Generic scene change handler
        """
        if DEBUG:
            self.logger.log("Scene changed " + str(self.sceneIndex))
        self.controller.sendSysex(self.builder.getSceneData(self.sceneIndex))
    
    def nameChanged(self):
        if self.scene.name != self.sceneName:
            if DEBUG:
                self.logger.log("Scene name changed : " + self.scene.name + " from " + self.sceneName)
            self.sceneName = self.scene.name
            data = SCENE_NAME + Utils.intToMidi(self.sceneIndex) + Utils.translateString(self.scene.name)
            self.controller.sendSysex(data)
        else:
            if DEBUG:
                self.logger.log("Scene nameChanged invoked but name is the same")
       
class SceneSysexBuilder:
    def getSceneData(self,sceneIndex):
        scene = Utils.getScene(sceneIndex)
        
        if scene:
            return self.buildSceneData(sceneIndex,scene)
        else:
            return NO_SCENE + Utils.intToMidi(sceneIndex)
        
    def buildSceneData(self,index,scene):
        nameData = Utils.translateString(scene.name)
        data = SCENE_DATA + Utils.intToMidi(index) + nameData
        return data
    