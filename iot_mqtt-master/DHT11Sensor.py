import Adafruit_DHT
from PublishDataMqtt import publish_To_Topic
import datetime as dt
import json

DHT_SENSOR = Adafruit_DHT.DHT11
DHT_PIN = 4

HUMIDITY_TOPIC = "uet/sensor/dht11/humidity"
TEMPERATURE_TOPIC = "uet/sensor/dht11/temperature"

currentTime = dt.datetime.now()
flag = 0

def getSensorData():
    humidity = None
    temperature = None
    humidityType = "%"
    temperatureType = "*C"

    while (humidity == None and temperature == None):
        humidity, temperature = Adafruit_DHT.read(DHT_SENSOR, DHT_PIN)
        print(humidity, temperature)
    
    humidityPayload = {
        "value": humidity,
        "dataType": humidityType
    }

    temperaturePayload = {
        "value": temperature,
        "dataType": temperatureType
    }
    publish_To_Topic(HUMIDITY_TOPIC, json.dumps(humidityPayload))
    publish_To_Topic(TEMPERATURE_TOPIC, json.dumps(temperaturePayload))
    

while(True):
    delta = dt.datetime.now() - currentTime

    if (flag == 0):
        getSensorData()
        print(flag)
        flag = 1

    if delta.seconds >= 900:
        flag = 0
        currentTime = dt.datetime.now()

