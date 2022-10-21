#!/usr/bin/env python

import os
import sys
import time

os.system("nohup mvn -f /home/pi/Desktop/iot_admin_backend spring-boot:run > /home/pi/Desktop/backend.txt 2>&1 &")
time.sleep(30)
os.system("nohup npm start --prefix /home/pi/Desktop/iot_admin_frontend > /home/pi/Desktop/frontend.txt 2>&1 &")
time.sleep(30)
os.system("nohup python3 /home/pi/Desktop/iot_mqtt/MqttReceiver.py > /home/pi/Desktop/receiver.txt 2>&1 &")
