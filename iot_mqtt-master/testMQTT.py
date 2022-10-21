# -*- coding: utf-8 -*-
import paho.mqtt.client as mqtt
import random, threading, json
from datetime import datetime

# MQTT Settings 
#MQTT_Broker = "localhost"
MQTT_Broker = "mqtt.iotwithus.com"
MQTT_Port = 1883
Keep_Alive_Interval = 45
MQTT_Topic_Humidity = "Iot/Sensor"
MQTT_Topic_Temperature = "Home/BedRoom/DHT22/Temperature"
MQTT_Topic_Switch = "CyberLink/commands/1037600"
MQTT_Topic_Register = "Iot/Infra/Register"
fakeTopic = "fasdf"

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
mqttc.connect(MQTT_Broker, int(MQTT_Port), int(Keep_Alive_Interval))		

		
def publish_To_Topic(topic, message):
	mqttc.publish(topic,message)
	print("Published: " + str(message) + " " + "on MQTT Topic: " + str(topic))
	print("")

def publish_data_to_switch():
	switchData = {
	 	"id":"0",
	 	"data":{
	 		"out1":0,
	 		"out2":100,
	 		"out3":0
	 	}
	}
	# data = {
	# 	"value": "123",
	# 	"dataType": "*C",
	# }
	data = {
		"name": "đèn phòng ngủ",
		"command": 100
	}
	switchDataJson = json.dumps(data)
	publish_To_Topic ("iot/control", switchDataJson)

# infraRegister()	
publish_data_to_switch()


