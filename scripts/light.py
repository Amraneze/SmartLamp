import RPi.GPIO as GPIO
GPIO.setmode(GPIO.BOARD)
import time

Light = 7

GPIO.setup(Light,GPIO.OUT)

def turnLightOn():
        print("turnLightOn")
	#GPIO.output(Light,GPIO.HIGH)
	GPIO.output(7, True)

def turnLightOff():
        print("turnLightOff")
	#GPIO.output(Light,GPIO.LOW)
	GPIO.output(7, False)
	
def cleanUpGPIO():
        print("CleanUp")
        GPIO.cleanup()
