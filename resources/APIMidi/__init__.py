import sys
from Demo import Demo

errorLog = open("stderr.txt", "w")
errorLog.write("Starting Error Log")
sys.stderr = errorLog
stdoutLog = open("stdout.txt", "w")
sys.stdout = stdoutLog

pythonInstallDir = 'C:\\Python22'
if pythonInstallDir not in sys.path:
    sys.path.append(pythonInstallDir)
    
defaultPythonPaths = ['\\DLLs', '\\lib', '\\lib\\lib-tk', '\\lib\\site-packages', '\\lib\\site-packages\\win32', '\\lib\\plat-win']
for dir in defaultPythonPaths:
    path = pythonInstallDir + dir
    if path not in sys.path:
        sys.path.append(path)

def create_instance(c_instance):
    """
    Called by Live when the device is selected from the menu.
    We create and return an object which handles the communication between our code and Live.  
    """
    return Demo(c_instance, stdoutLog)
