from consts import *
import Utils
import Live

class SongDataHandler:
    """
    Handles request for song data on the assigned channel via midi cc messages
    """
    def __init__(self,channel,controller,logger):
        self.controller = controller
        self.logger = logger
        self.channel = channel
        self.builder = SongSysexBuilder()
        if DEBUG:
            self.logger.log("SongDataHandler " + str(self.channel))
        
    def build_midi_map(self,script_handle,midi_map_handle):
        map_mode = Live.MidiMap.MapMode.absolute
        Live.MidiMap.forward_midi_cc(script_handle,midi_map_handle,self.channel,SONG_DATA_CC)
    
    def handle_cc(self,chan,num,val):
        """
        Only need to use the value of the cc which is the track index.
        """
        if DEBUG:
            self.logger.log("SongData " + str(num) + "," + str(val))
        return self.builder.getSongData()
    
    def can_handle(self,chan,num,val):
        return chan == self.channel and num == SONG_DATA_CC
        
class SongSysexBuilder:
    def getSongData(self):
        song = Utils.getSong()
        
        if song:
            return self.buildSongData(song)
        else:
            return None
        
    def buildSongData(self,song):
        
        #nameData = Utils.translateString(song.name)
        # Unfortunately songs dont let you access the name.
        nameData = Utils.translateString("The Song")
        # Flags
        flags = song.is_playing
        flags += song.metronom << 1
        flags += song.overdub << 2
        flags += song.punch_in << 3
        flags += song.punch_out << 4
        flags += song.record_mode << 5

        numTracks = len(song.tracks) & 0xF7
        numScenes = len(song.scenes) & 0xF7
        numCuePoints = len(song.cue_points) & 0xF7

        sigDenominator = song.signature_denominator
        sigNumerator = song.signature_numerator
        
        #songTime = Utils.floatToMidi(song.current_song_time)
        #groove = Utils.floatToMidi(song.groove_amount)
        #loopStart = Utils.floatToMidi(song.loop_start)
        #loopLength = Utils.floatToMidi(song.loop_length)
        #songLength = Utils.floatToMidi(song.song_length)
        #tempo = Utils.floatToMidi(song.tempo)
        
        #data = SONG_DATA + (flags,numTracks,numScenes,numCuePoints,sigDenominator,sigNumerator,songTime,groove,loopStart,loopLength,songLength,tempo) + nameData
        data = SONG_DATA + (flags,numTracks,numScenes,numCuePoints,sigDenominator,sigNumerator) + nameData
        return data
    