# Global debug flag
DEBUG = 0

MAX_TRACKS = 127

# Midi Status Bytes

NOTE_OFF_STATUS = 128
NOTE_ON_STATUS = 144
CC_STATUS = 176

# Midi Request Channels
# Note clips and scenes take a full channel for each operation
CLIP_TRIGGER_CHANNEL = 0
CLIP_DATA_CHANNEL = 1
CLIP_MONITOR_CHANNEL = 2
CLIP_STOP_MONITOR_CHANNEL = 3

GENERAL_REQUEST_CHANNEL = 4
DEVICE_DATA_CHANNEL = 5

SCENE_TRIGGER_CHANNEL = 10
SCENE_DATA_CHANNEL = 11
SCENE_MONITOR_CHANNEL = 12
SCENE_STOP_MONITOR_CHANNEL = 13

# Midi Request CC's for the general request channel
SONG_DATA_CC = 0

TRACK_DATA_CC = 1
TRACK_MONITOR_CC = 2
TRACK_STOP_MONITOR_CC = 3

SCENE_DATA_CC = 10
SCENE_MONITOR_CC = 11
SCENE_STOP_MONITOR_CC = 12
FIRE_SCENE_CC = 13

MIXER_DATA_CC = 20

CUEPOINT_DATA_CC = 30
CUEPOINT_MONITOR_CC = 31
CUEPOINT_STOP_MONITOR_CC = 32
CUEPOINT_FIRE_CC = 33

NUM_TRACKS_CC = 100

# SYSEX DATA DECLARATIONS
# Define start / exit sysex messages. Only the start 240 and end 247 are required
# So we can use the second byte to say what type of message it is. In this case
# im using 0 for information and 1 for response data
WELCOME_SYSEX_MESSAGE = (240,0,72,69,76,76,79,247)
BUILD_MAP_SYSEX_MESSAGE = (240,0,66,85,73,76,68,247)
GOODBYE_SYSEX_MESSAGE = (240,0,66,89,69,247)

SYSEX_BEGIN = (240,)
SYSEX_END = (247,)

CLIP_DATA = (1,)
EMPTY_CLIP_SLOT = (2,)
CLIP_IS_TRIGGERED = (3,)
CLIP_IS_PLAYING = (4,)
CLIP_NAME = (5,)
CLIP_IS_LOOPING = (6,)
CLIP_COLOR = (7,)

TRACK_DATA = (10,)
NO_TRACK_DATA = (11,)
TRACK_NAME = (12,)
TRACK_IS_ARMED = (13,)
TRACK_IS_MUTED = (14,)
TRACK_IS_SOLOED = (15,)

DEVICE_DATA = (20,)
NO_DEVICE_DATA = (21,)

SCENE_DATA = (30,)
NO_SCENE_DATA = (31,)
SCENE_NAME = (32,)

CUEPOINT_DATA = (40,)
NO_CUEPOINT_DATA = (41,)
CUEPOINT_NAME = (42,)
CUEPOINT_TIME = (43,)

MIXER_DATA = (50,)
NO_MIXER_DATA = (51,)

SONG_DATA = (60,)

NUM_TRACKS = (100,)