import RPi.GPIO as GPIO
import time

GPIO.setmode(GPIO.BOARD)
GPIO.setup(7, GPIO.OUT)

while (True):
    try:
        GPIO.output(7, True)
        time.sleep(0.5)
        GPIO.output(7, False)
        time.sleep(0.5)
    except KeyboardInterrupt:
        print "disconnecitng"
        GPIO.cleanup()
        print "all done"
        break
