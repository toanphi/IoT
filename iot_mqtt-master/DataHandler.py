#!/usr/bin/env python
# -*- coding: utf-8 -*- 
import json
# import paho.mqtt.client as mqtt
from datetime import datetime
from HandleControlReq import controlling
import requests
import time

handlerTopic = "iot/control"
requestTopic = "iot/request"
switchStateTopic = "CyberLink/input/json"
requestDeviceTopic = "iot/device"
BASE_URL = "http://localhost:3123"
MOBILE_URL = "http://localhost:5000"
# get all devices

def login():
    headers = {'Content-type': 'application/json'}
    item = {
        "username": "admin",
        "password": "uet@iot@admin"
    }
    accessToken = requests.post(BASE_URL + "/authenticate", data = json.dumps(item), headers = headers).json()['data']['jwt']
    return accessToken

accessToken = login()

def handleSensorInput(topic, jsonData):
    headers = {'Content-type': 'application/json', "Authorization": accessToken}
    listDevice = requests.get(BASE_URL + "/device", headers = headers).json()['data']
    for sen in listDevice:
        if (sen['productType'] == "sensor" and sen['controlTopic'] == topic):
            insertOutputData(sen, jsonData)

def insertOutputData(device, jsonData):

    jsonDict = json.loads(jsonData)
    value = jsonDict['value']
    dataType = jsonDict['dataType']
    rawDate = datetime.today()
    # createdDate = datetime.strftime(rawDate, "%Y-%m-%d %H:%M:%S")
    item = {
        "deviceId": device["id"],
        "data": value,
        "dataType": dataType,
        "createdDate": str(rawDate),
        "id": 0
    }
    headers = {'Content-type': 'application/json', "Authorization": accessToken}
    a = requests.post(BASE_URL + "/device/data", data = json.dumps(item), headers = headers)

def dataHandler(topic, jsonData):
    if ("uet/sensor" in topic):
        print(123123123)
        handleSensorInput(topic, jsonData)
    if (topic == handlerTopic):
        try:
            jsonDataTmp = jsonData        
            if (b"\x00" in jsonData):
                jsonDataTmp = jsonData.replace(b'\x00',b'')
            data = json.loads(jsonDataTmp)
            print(data)
            did = {
                "id": data['id']
            }
            headers = {'Content-type': 'application/json', "Authorization": accessToken}
            item = requests.get(BASE_URL + "/device/by/{id}".format(**did), headers = headers).json()["data"]
            controlling(data, item)
        except Exception as e:
            print("Handler exception", e)
    
