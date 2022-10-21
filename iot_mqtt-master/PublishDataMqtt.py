# -*- coding: utf-8 -*-
import paho.mqtt.client as mqtt
import random, threading, json
from datetime import datetime
import time

# MQTT Settings 
MQTT_Broker = "localhost"
# MQTT_Broker = "222.252.62.94"
# MQTT_Broker = "192.168.1.4"
MQTT_Port = 1883
Keep_Alive_Interval = 45
MQTT_Topic_Humidity = "Iot/Sensor"
MQTT_Topic_Temperature = "Home/BedRoom/DHT22/Temperature"
MQTT_Topic_Switch = "CyberLink/commands/1037600"
MQTT_Topic_Register = "Iot/Infra/Register"

def on_connect(client, userdata, rc,  properties=None):
    if rc != 0:
        print("Unable to connect to MQTT Broker...")
    else:
        print("Connected with MQTT Broker: " + str(MQTT_Broker))

def on_publish(client, userdata, mid):
    pass
        
def on_disconnect(client, userdata, rc):
    if rc !=0:
        print("pass")
        
mqttc = mqtt.Client()
mqttc.on_connect = on_connect
mqttc.on_disconnect = on_disconnect
mqttc.on_publish = on_publish

mqttc.username_pw_set(username="username",password="@Iot123456")

        
def publish_To_Topic(topic, message):
    mqttc.connect(MQTT_Broker, int(MQTT_Port), int(Keep_Alive_Interval))        
    mqttc.publish(topic,message)
    print("Published: " + str(message) + " " + "on MQTT Topic: " + str(topic))
    print("")

def publish_data_to_switch(flag):
    switchData1 = {
        "id":"23",
        "command": "KEY_OFF"
    }
    switchData = {
        "id":"23",
        "command": "KEY_ON"
    }
    # data = {
    #   "value": "123",
    #   "dataType": "*C",
    # }
    data = {
        "data": "devices"
    }
    
    datasw = {
        "id":"1037600",
        "data":{"out1":0,"out2":100,"out3":0}
        }
    switchDataJson = {}
    if (flag == 1):
        switchDataJson = json.dumps(switchData)
    else:
        switchDataJson = json.dumps(switchData1)
    # print(type(switchDataJson))
    #publish_To_Topic("iot/control", switchDataJson)
    publish_To_Topic(MQTT_Topic_Switch, switchDataJson)

# infraRegister()
#flagg = 1
#for i in range(0, 35):
    
#    publish_data_to_switch(flagg)
     
#    flagg = flagg * (-1)
#    time.sleep(2)  

