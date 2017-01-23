# -*- coding: utf-8 -*-
"""
Created on Tue Oct 25 14:49:32 2016

@author: Dave Ho
"""

#HOST&&PORT: http://127.0.0.1:5000/
#COMP1: 192.168.1.54
#COMP2: 192.168.1.77

import sqlite3
import flask
import json as mjson
from flask import Flask
from flask import request
import ast

app = Flask(__name__)

DATABASE = 'database.db'

#Tester
@app.route("/", methods=["GET", "POST"])
def hello():
    #if request.method == "GET":
    print "someone said get"
    return "JJ!"
    '''
    if request.method == "POST":
        content = request.get_json(silent=True)
        print content
        print "someone posted something"
        return ""
    '''

@app.route("/json", methods=['GET', 'POST'])
def json():
    dickeys = request.form.keys()
    dic = ast.literal_eval(dickeys[0])
    conn = sqlite3.connect(DATABASE)
    c = conn.cursor()
    #users cannot be duplicate
    try:
        c.execute("INSERT INTO users VALUES('"+dic['phone']+"', '"+dic['phone']+"')")
    except sqlite3.IntegrityError:
        pass
    c.execute('SELECT COUNT(pid) FROM path')
    count = c.fetchone()[0]
    c.execute("INSERT INTO path VALUES('"+str(count)+"', '"+dic['phone']+"', '"+dic['title']+"', '"+dic['zipCodeList'][0]+"')")   
    for i in range(0, len(dic['lat'])):
        c.execute("INSERT INTO points VALUES('"+str(count)+"', '"+str(dic['lat'][i])+"', '"+str(dic['lng'][i])+"', '"+str(i)+"')")
    for i in range(0, len(dic['markerMap'].keys())):
        key = sorted(dic['markerMap'].keys())[i]
        c.execute("INSERT INTO markers VALUES('"+str(count)+"', '"+str(dic['markerMap'][key]['lat'])+"', '"+str(dic['markerMap'][key]['lng'])+"', '"+dic['markerMap'][key]['description']+"', '"+dic['markerMap'][key]['image']+"')")
    conn.commit()
    conn.close()
    return request.json

@app.route("/getTitle", methods=["GET", "POST"])
def getTitle():
    """
    Example JSON Call: http://192.168.1.65/getTitle?zip=95135
    Android User Given: Zip Code of Current User Location
    What Server Needs to return: Given zip code of current user location, give back set of paths within that location using json
    use paths table
    """
    if request.method == "GET":
        conn = sqlite3.connect(DATABASE)
        c = conn.cursor()
        zipcode = str(request.args.get('zipCode'))
        # need to return titles back to android user from database
        resultString = ""
        for row in c.execute("SELECT * FROM path WHERE zip="+zipcode):
            resultString += str(row[1])+"_"+str(row[2])+"\n"
        return resultString

@app.route("/getDetail", methods=["GET", "POST"])
def getDetail():
    """
    Example JSON Call: http://192.168.1.65/getDetail?phonenumber=14083346432&id=hi
    Android User Given: Phone + title of path selected
    What Server Needs to return: given phone and title of path return the dictionary json of that path
    user users table
    """
    if request.method == "GET":
        conn = sqlite3.connect(DATABASE)
        c = conn.cursor()
        # need to return full details back to android user from database
        phone = "\"" + str(request.args.get('phoneNumber')) + "\""
        titleid = "\"" + str(request.args.get('id')) + "\""
        c.execute('SELECT pid, zip FROM path WHERE name='+phone+' AND title='+titleid)
        tmp = c.fetchone()
        path_id = tmp[0]
        zipCodeList = [tmp[1]]
        points_list = []
        for row in c.execute('SELECT lat, lng FROM points WHERE pid='+str(path_id)+" ORDER BY sequence"):
            points_list.append(row)
        marker_list = []
        for row in c.execute('SELECT lat, lng, description, image FROM markers WHERE pid='+str(path_id)+" ORDER BY sequence"):
            marker_list.append(row)      
        lat = []
        lng = []
        for coordinate in points_list:
            lat.append(coordinate[0])
            lng.append(coordinate[1])
        jsondic = {}
        jsondic["lat"] = lat
        jsondic["lng"] = lng
        count = 1;
        markerMap = {}
        for marker in marker_list:
            snap = {}
            snap["lat"] = marker[0]
            snap["lng"] = marker[1]
            snap["description"] = marker[2]
            snap["image"] = marker[3]
            markerMap["Snap"+str(count)] = snap
            count += 1
        
        jsondic["markerMap"] = markerMap
        jsondic["zipCodeList"] = zipCodeList        
        

        return mjson.dumps(jsondic)



if __name__ == "__main__":
    app.run(host='0.0.0.0', port=80)
    #app.run(host='0.0.0.0',port="80")