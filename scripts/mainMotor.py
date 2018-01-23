import RPi.GPIO as GPIO
GPIO.setmode(GPIO.BOARD)
import motor
import time
import os
import sys
import termios
import tty

def getCh():
	fd = sys.stdin.fileno()
	old_settings = termios.tcgetattr(fd)
	
	try:
		tty.setraw(fd)
		ch = sys.stdin.read(1)
	finally:
		termios.tcsetattr(fd, termios.TCSADRAIN, old_settings)
		
	return ch
    
speed = 2500

while (True):
    try:
        motor.piTest(speed)
        print ("Current speed: %d" % speed)
        time.sleep(0.5)
        #motor.forward()
        #time.sleep(0.5)
        c = getCh()
        if c == '+':
            speed = speed + 1;
        if c == '-':
            speed = speed - 1;
    except KeyboardInterrupt:
        print "cleanup"
        GPIO.cleanup()
        print "all done"
        break


