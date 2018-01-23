import RPi.GPIO as GPIO
GPIO.setmode(GPIO.BOARD)
import time
	
def cleanUpGPIO():
        print("CleanUp")
        GPIO.cleanup()