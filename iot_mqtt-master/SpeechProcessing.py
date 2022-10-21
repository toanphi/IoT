#!/usr/bin/env python
# -*- coding: utf-8 -*- 
import speech_recognition as sr
import webbrowser
import subprocess
import requests
from PublishDataMqtt import publish_To_Topic
from gtts import gTTS
#import winsound
from playsound import playsound
from HandleControlReq import controlling
import os
import json
from datetime import datetime

controlTopic = "iot/control"
BASE_URL = "http://192.168.1.4:3123"
dictDevice = {}

def login():
    headers = {'Content-type': 'application/json'}
    item = {
        "username": "service",
        "password": "uet@iot@service"
    }
    accessToken = requests.post(BASE_URL + "/authenticate", data = json.dumps(item), headers = headers).json()['data']['jwt']
    return accessToken

accessToken = login()

def fetchDevice():
    headers = {'Content-type': 'application/json', "Authorization": accessToken}
    devices = requests.get(BASE_URL + "/device", headers = headers).json()['data']
    for dev in devices:
        name = dev['name']
        dictDevice[name] = dev
    return devices

def get_audio():
    r = sr.Recognizer()
    with sr.Microphone() as source:
        # print(" ",5 end='')
        audio = r.listen(source, phrase_time_limit=5)
        try:
            text = r.recognize_google(audio, language="vi-VN")
            print(text)
            return text
        except:
            print("ready")
            return ""

def getLstCommand(productType):
    lst = []
    for item in lstCommand:
        if (item['type'] == productType):
            lstPhrases = item['phraseCommand'].split(":")
            for phrase in lstPhrases:
                lst.append(phrase)    
    return lst

# To use, call "dieu khien" + device to start controll the device. eg: "dieu khien den"
#
def getCommand():
    #tts("xin chào, hệ thống đang hoạt động")
    lstDevice = fetchDevice()
    selectedDevice = 0
    dictCommand = {}
    isActive = False
    while (True):
        ## get audio
        text = get_audio().lower()
        time = datetime.now()
        # text = "điều khiển tivi"
        if not text:
            continue
        elif welcomeQ(text) and isActive == False:
            login()
            hour = time.hour
            if (hour >= 6 and hour <= 10):
                tts("Chào buổi sáng")
            elif (hour >= 11 and hour <= 13):
                tts("Chào buổi trưa")
            elif (hour >= 14 and hour <= 17):
                tts("Chào buổi chiều")
            elif (hour >= 18 and hour <= 24):
                tts("Chào buổi tối")
            tts("tôi có thể giúp gì cho bạn?")
            isActive = True
            continue
        if isActive:
            # selectedDevice = getDevice(text)
            if (byeQ(text)):
                isActive = False
                tts("tạm biệt bạn")
                continue
            if ("mấy giờ" in text):
                hour = time.hour
                minute = time.minute
                tts(f'bây giờ là {hour} giờ {minute} phút')
                continue
            
            if ("thiết bị" in text):
                lstDevice = fetchDevice()
                count = 1
                if (len(lstDevice) == 0):
                    tts("Không có thiết bị, vui lòng đăng kí để sử dụng.")
                    continue
                else:
                    tts("các thiết bị hiện có là")
                    for device in lstDevice:
                        tts(f'số {count}')
                        tts(device['name'])
                        try:
                            tts(device['description'])
                        except:
                            print("no desc")
                        count+=1
                continue
            if (dictDevice.get(text, 0) != 0):
                selectedDevice = dictDevice.get(text, 0)
                if (selectedDevice == 0):
                    continue
                productType = selectedDevice['productType']
                if (productType == "sensor"):
                    headers = {'Content-type': 'application/json', "Authorization": accessToken}
                    lstCommand = requests.get(BASE_URL + f'/device/command/{productType}', headers = headers).json()['data']
                    continue
                headers = {'Content-type': 'application/json', "Authorization": accessToken}
                lstCommand = requests.get(BASE_URL + f'/device/command/{productType}', headers = headers).json()['data']
                for item in lstCommand:
                    lstPhrases = item['phraseCommand'].split(":")
                    for phrase in lstPhrases:
                        dictCommand[phrase] = item
                deviceName = selectedDevice['name']
                tts(f"đã chọn {deviceName}")
                continue
            if (selectedDevice != 0):
                userCommand = dictCommand.get(text, 0)
                if (userCommand == 0):
                    #tts(f"yêu cầu không phù hợp với thiết bị {selectedDevice['.name']}")
                    deviceName = selectedDevice['name']
                    tts(f"yêu cầu không phù hợp với thiết bị {deviceName}")
                    continue
                else:
                    jsonData = {
                        "id": selectedDevice['id'],
                        "command": userCommand['command']
                    }
                    print(jsonData)
                    publish_To_Topic(controlTopic, json.dumps(jsonData))
                    # res = controlling(jsonData, selectedDevice)
                    continue

            # excLstCommand = getLstCommand(selectedDevice['productType'])
                # deviceName = selectedDevice['name']
                # tts(f'Đang thực hiện điều khiển {deviceName}')
            # if (selectedDevice != "" and selectedDevice != 0):
                # if ("liệt kê chức năng" in text):
                #     tts("các chức năng bao gồm:")
                #     for command in excLstCommand:
                #         tts(command[''])
            # for cmd in excLstCommand:
            #     if (cmd in text):
            #         tts("đang thực hiện chức năng " + text)
            #         jsonData = {
            #             "id": selectedDevice['id'],
            #             "command": cmd
            #         }
            #         print(jsonData)
            #         # controlling(cmd)
            #         break

def welcomeQ(text):
    return "hello" in text or "xin chào" in text or "chào" in text

def byeQ(text):
    return "bye" in text or "tạm biệt" in text or "kết thúc" in text

def tts(textIn):
    language='vi'
    myobj=gTTS(text=textIn,lang=language,slow=False)
    myobj.save("soundtrack.mp3")
    filee = "soundtrack.mp3"
    # os.system("afplay " + file + "&")
    os.system('mpg321 ' + filee + ' &')
    # playsound(file)
    # os.remove("soundtrack.mp3")

# fetchDevice()
getCommand()


