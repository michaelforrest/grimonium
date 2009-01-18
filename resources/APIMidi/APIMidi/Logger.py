try:
    import socket
except:
    print "No Sockets"
    
class Logger:
    """
    Simple logger.
    Tries to use a socket which connects to localhost port 4444 by default.
    If that fails then it logs to a file
    """
    def __init__(self,stdout):
        self.stdout = stdout
        try:
            self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        except:
            print "Couldn't create socket"
            self.socket = None
            
        self.connected = 0
        
        if self.socket:
            try:
                self.socket.connect(("localhost", 4444))
                self.connected = 1
            except:
                print "Couldn't connect socket"

    def log(self,msg):
        #self.stdout.write(msg + "\n")
        if self.connected:
            self.send(msg)
        else:
            print(msg)
        
    def send(self,msg):
        if self.connected:
            self.socket.send(msg + "\n")
    
    def close(self):
        if self.connected:
            self.socket.send("Closing..")
            self.socket.close()
