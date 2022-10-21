import paho.mqtt.client as mqtt
from DataHandler import dataHandler

# MQTT Settings
MQTT_Broker = "localhost"
#MQTT_Broker = "mqtt.iotwithus.com"
MQTT_Port = 1883
Keep_Alive_Interval = 45
MQTT_Topic = "#"
print("Receiver on")
#Subscribe to all Sensors at Base Topic
def on_connect(mosq, obj, rc, properties=None):
	mqttc.subscribe(MQTT_Topic, 0)

#Save Data into DB Table
def on_message(mosq, obj, msg):
	# print("MQTT Data Received...")
	# print("MQTT Topic: " + msg.topic)
	# print("Data: " + str(msg.payload))
	dataHandler(msg.topic, msg.payload)

def on_subscribe(mosq, obj, mid, granted_qos):
    pass

mqttc = mqtt.Client()

# Assign event callbacks
mqttc.on_message = on_message
mqttc.on_connect = on_connect
mqttc.on_subscribe = on_subscribe

# Connect
# mqttc.username_pw_set(username="username",password="@Iot123456")
mqttc.connect(MQTT_Broker, int(MQTT_Port), int(Keep_Alive_Interval))
# mqttc.session.retry_interval(45)

# Continue the network loop
mqttc.loop_forever()
