import pigpio
from time import sleep

Motor1A = 16
Motor1B = 18
Motor1E = 15
Motor1F = 15

pi = pigpio.pi()

pi.set_mode(Motor1A, pigpio.OUTPUT)
pi.set_mode(Motor1B, pigpio.OUTPUT)
pi.set_mode(Motor1E, pigpio.OUTPUT)
pi.set_mode(Motor1F, pigpio.OUTPUT)
pi.set_PWM_range(9,100)

pi.write(Motor1A, 0)
pi.write(Motor1B, 1)
pi.write(Motor1E, 1)
pi.write(Motor1F, 1)

pi.set_PWM_dutycycle(Motor1E, 0)
sleep(1)
print("Duty cycle 25")
pi.set_PWM_dutycycle(Motor1E, 25)
sleep(1)
print("Duty cycle 40")
pi.set_PWM_dutycycle(Motor1E, 40)
sleep(1)
print("Duty cycle 80")
pi.set_PWM_dutycycle(Motor1E, 80)
sleep(1)
print("Duty cycle 100")
pi.set_PWM_dutycycle(Motor1E, 100)
sleep(1)
pi.stop()