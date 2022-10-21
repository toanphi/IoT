from bs4 import BeautifulSoup
import requests
import wget
import re
import pymongo


url =  'http://lirc-remotes.sourceforge.net/remotes-table.html?fbclid=IwAR2Nyaz0emB40tum4zYIu_Mo3huokH77O6jxLFmgh9D-pTpPrH2vYkZsJIg'
dest = './supported_name.txt'

def writeToFile(name):
    output = open(dest, 'a')
    output.write(name+"\n")

page = requests.get(url)
soup = BeautifulSoup(page.text, 'html.parser')

lstTr = soup.findAll("table")[2].findAll("tr")
myclient = pymongo.MongoClient("mongodb://localhost:27017/")
mydb = myclient["IOT"]
mycol = mydb["supported_device"]
count = 0
finished = []
for tr in lstTr[1:]:
    # link = tr.findAll("a", text=re.compile('.*conf'))[0]['href']
    # dlLink = link + "?format=raw"
    # wget.download(dlLink, "./list_device")
    # writeToFile(item)
    name = tr.findAll("td")[3].text
    if (name not in finished):
        finished.append(name)
        count+=1
        x = mycol.insert_one({"name": name})