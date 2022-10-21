import speech_recognition as sr
import webbrowser
import subprocess
import requests
# from PublishDataMqtt import publish_To_Topic
from gtts import gTTS
from playsound import playsound
from HandleControlReq import controlling
import os


BASE_URL = "http://localhost:3123"
lstDevice = requests.get(BASE_URL + "/device/get/all").json()['data']
lstCommand = requests.get(BASE_URL + "/device/command/get/all").json()['data']

def get_audio():
    r = sr.Recognizer()
    with sr.Microphone() as source:
        print(" ", end='')
        audio = r.listen(source, phrase_time_limit=5)
        try:
            text = r.recognize_google(audio, language="vi-VN")
            print("try")
            print(text)
            return text
        except Exception as err:
            print("error: ", err)
            print("dead")
            return ""

def isDeviceAvailable(name):
    for device in lstDevice:
        if device['name'] in name:
            return device
    return 0

def getLstProp(prop, listt):
    lst = []
    for item in listt:
        lst.append(item[prop])
    return lst

# To use, call "dieu khien" + device to start controll the device. eg: "dieu khien den"
#
def getCommand():
    while (True):
        ## get audio
        text = get_audio()

        if (text == ""):
            continue

        selectedDevice = isDeviceAvailable(text)
        if (selectedDevice == 0):
            tts("Không tìm thấy thiết bị")
            continue
        else:
            tts("Đang thực hiện điều khiển " + selectedDevice['name'])
            pushSignal(selectedDevice)

        if ("chức năng hiện tại" in text):
            tts("không có chức năng")
        if ("liệt kê thiết bị" in text):
            tts("các thiết bị hiện có là")
            for device in lstDevice:
                tts(device['name'])

def pushSignal(device):
    if (device['controlType'] == "wireless"):
        dType = {
            "type": device['productType']
        }
    else:
        dType = {
            "type": device['remoteType']
        }
    specCommand = requests.get(BASE_URL + "/device/command/get/{type}".format(**dType))
    while (True):
        ## get audio
        text = get_audio()
        if (text == ""):
            continue
        ## defind condition to exit
        if ("kết thúc điều khiển" in text): 
            tts("Đã kết thúc điều khiển")    
            return
        if ("chức năng hiện tại" in text):
            tts("Hiện tại đang thực hiện điều khiển " + device['name'])  
            continue  
        command = device['name']+ "_" + text
        tts(controlling(command, lstDevice))

def tts(textIn):
    language='vi'
    myobj=gTTS(text=textIn,lang=language,slow=False)
    myobj.save("soundtrack.mp3")
    file = "soundtrack.mp3"
    # os.system("mpg123 " + file)
    playsound("soundtrack.mp3")

getCommand()
#get_audio()