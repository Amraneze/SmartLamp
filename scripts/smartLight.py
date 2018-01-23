import os
import glob
import RPi.GPIO as GPIO
from bluetooth import *
GPIO.setmode(GPIO.BOARD)
import time
import sys

GPIO.setmode(GPIO.BCM)
in1_pin1 = 23
in2_pin1 = 24
in1_pin2 = 17
in2_pin2 = 22

# The Pins. Use Broadcom numbers.
RED_PIN   = 17
GREEN_PIN = 22
BLUE_PIN  = 24

# Number of color changes per step (more is faster, less is slower).
# You also can use 0.X floats.
STEPS     = 1
bright = 255
r = 255.0
g = 0.0
b = 0.0

io.setup(in1_pin1, GPIO.OUT)
p1 = GPIO.PWM(in1_pin1, 50)
p1.start(0)
io.setup(in2_pin1, GPIO.OUT)
p2 = GPIO.PWM(in2_pin1, 50)
p2.start(0)
io.setup(in1_pin2, GPIO.OUT)
p3 = GPIO.PWM(in1_pin2, 50)
p3.start(0)
io.setup(in2_pin2, GPIO.OUT)
p4 = GPIO.PWM(in2_pin2, 50)
p4.start(0)

server_sock = BluetoothSocket( RFCOMM )
server_sock.bind(("", PORT_ANY))
server_sock.listen(1)

port = server_sock.getsockname()[1]

print port

uuid = "94f39d29-7d6d-437d-973b-fba39e49d4ee"
advertise_service (server_sock, "WashStServer",
                   service_id = uuid,
                   service_classes = [uuid, SERIAL_PORT_CLASS],
                   profiles = [SERIAL_PORT_PROFILE],)
connected = 0
while True:
    if (connected == 0):
        print "waiting for connection"
        client_sock, client_info = server_sock.accept()
        print "Accepted connection from ", client_info
        connected = 1

    try:
        data = client_sock.recv(1024)
        if len(data) == 0: break
        
        print "received data", data
        if data == 'L0':
            print "come light2 0"
            setRGBLights(0,0,0)
        elif data == 'L1':
            print "come light2 1"
            setRGBLights(255, 255, 255)
        elif 'RGB' in data:
            print "come RGB"
            rgb = data[4:].replace(")", "").split(',')
            print rgb
            setRGBLights(rgb[0], rgb[1], rgb[2])
        elif 'MONF' in data:
            startMotor(data[3:])
        elif 'MONB' in data:
            goingBackwardMotor(data[3:])
        elif 'MOFF' in data:
            stopMotor()
        else:
            print "come error"
            
    except IOError:
        print "error"
        connected = 0
        pass
    except KeyboardInterrupt:
        print "disconnecitng"
        client_sock.close()
        server_sock.close()
        connected = 0
        print "all done"

        break

def startMotor(speed):
    print("Going Forward")
    p1.start(speed)
    p2.start(0)
    p3.start(speed)
    p4.start(0)

def goingBackwardMotor(speed):
    print("Going backward")
    p1.start(0)
    p2.start(speed)
    p3.start(0)
    p4.start(speed)

def stopMotor():
    print("Going stop")
    p1.start(0)
    p2.start(0)
    p3.start(0)
    p4.start(0)

def setRGBLights(r, g , b):
    setLights(RED_PIN, r)
    setLights(GREEN_PIN, g)
    setLights(BLUE_PIN, b)


def setLights(pin, brightness):
    realBrightness = int(int(brightness) * (float(bright) / 255.0))
    pi.set_PWM_dutycycle(pin, realBrightness)


