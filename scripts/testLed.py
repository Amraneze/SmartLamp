import RPi.GPIO as GPIO
#GPIO.setmode(GPIO.BOARD)
GPIO.setmode(GPIO.BCM) 
import time

import os
import sys
import termios
import tty

Motor1A = 17

GPIO.setup(Motor1A,GPIO.OUT)


def getCh():
	fd = sys.stdin.fileno()
	old_settings = termios.tcgetattr(fd)
	
	try:
		tty.setraw(fd)
		ch = sys.stdin.read(1)
	finally:
		termios.tcsetattr(fd, termios.TCSADRAIN, old_settings)
		
	return ch

while True:
            c = getCh()
            GPIO.output(Motor1A,GPIO.HIGH)
            if c == 'c':
                GPIO.cleanup()
                exit()
	
def cleanUpGPIO():
        print("CleanUp")
        GPIO.cleanup()
