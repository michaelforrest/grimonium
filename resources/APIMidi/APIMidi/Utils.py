import Live
from consts import *
#from struct import *

def getSong():
    """Gets a the current Song instance"""
    return Live.Application.get_application().get_document()

def getScene(num):
    scenes = getSong().scenes
    
    if num < len(scenes):
        return scenes[num]
    else:
        return None
    
def getTrack(num):
    """Returns track number (num) (starting at 0)"""
    tracks = getSong().tracks
    
    if num < len(tracks):
        return tracks[num]
    else:
        return None

def getClip(track,scene):
    track = getTrack(track)
    if track and scene < len(track.clip_slots):
        clipSlot = track.clip_slots[scene]
        if clipSlot.has_clip:
            return clipSlot.clip
    return None

def getClipSlot(track,scene):
    track = getTrack(track)
    if track and scene < len(track.clip_slots):
        return track.clip_slots[scene]
    return None
    
def translateString(text):
    """
    Convert a string into a sysex safe string
    """
    result = ()
    length = len(text)
    for i in range(0,length):
        charCode = ord(text[i])
        if charCode < 32:
            charCode = 32
        elif charCode > 127:
            charCode = 127
        result = (result + (charCode,))
    return result

def mapAllCCs(script_handle, midi_map_handle, channel):
    for cc in range(127):
        Live.MidiMap.forward_midi_cc(script_handle,midi_map_handle,channel,cc)

def mapAllNotes(script_handle, midi_map_handle, channel):
    for note in range(127):
        Live.MidiMap.forward_midi_note(script_handle,midi_map_handle,channel,note)
    
def intToMidi(int):
    """
    Convert an unsigned int to a tuple containing two 7-bit bytes.
    Max int value is 14 bits i.e. 16384
    """
    msb = (int >> 7) & 0x7F
    lsb = int & 0x7F
    return (msb,lsb)
    
#def midiToFloat(midi):
#    """
#    Convert a 5 byte list of midi data (i.e. 7 bit bytes) to a float
#    """
#    #m1 = midi[0]
#    m2 = midi[1]
#    m3 = midi[2]
#    m4 = midi[3]
#    #m5 = midi[4]
#    
#    # bottom 4 bits of m1 and top 4 bits of m2
#    o1 = ((midi[0] & 0x0F) << 4) + ((m2 & 0x78) >> 3)
#    # bottom 3 bits of m2 and top 5 bits of m3
#    o2 = ((m2 & 0x07) << 5) + ((m3 & 0x7C) >> 2)
#    # bottom 2 bits of m3 and top 6 bits of m4
#    o3 = ((m3 & 0x03) << 6) + ((m4 & 0x7E) >> 1)
#    # bottom bit of m4 and m5
#    o4 = ((m4 & 0x01) << 7) + midi[4]
#    
#    return unpack("f",chr(o1) + chr(o2) + chr(o3) + chr(o4))
#
#def floatToMidi(x):
#    """
#    Convert a float to midi, a list of 5 7-bit bytes
#    """
#    s = pack("f",x)
#    b1 = ord(s[0])
#    b2 = ord(s[1])
#    b3 = ord(s[2])
#    b4 = ord(s[3])
#    
#    #print(hex(b1) + " " + hex(b2) + " " + hex(b3) + " " + hex(b4))
#    #print(tobin(b1) + tobin(b2) + tobin(b3) + tobin(b4))
#    
#    # Create 5 7-bit bytes.
#    # Top 4 bits of b1
#    o1 = (b1 & 0xF0) >> 4;
#    # bottom 4 bits of b1 and top 3 of b2
#    o2 = ((b1 & 0x0F) << 3) + ((b2 & 0xE0) >> 5)
#    # bottom 5 of b2 and top 2 of b3
#    o3 = ((b2 & 0x1F) << 2) + ((b3 & 0xC0) >> 6)
#    # bottom 6 of b3 and top bit of b4
#    o4 = ((b3 & 0x3F) << 1) + ((b4 & 0x80) >> 7)
#    # bottom 7 of b4
#    o5 = b4 & 0x7F;
#    
#    #print(tobin(o1,4) + tobin(o2,7) + tobin(o3,7) + tobin(o4,7) + tobin(o5,7))
#
#    return [o1,o2,o3,o4,o5]
