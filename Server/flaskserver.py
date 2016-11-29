# -*- coding: utf-8 -*-
"""
Created on Tue Oct 25 14:49:32 2016

@author: Dave Ho
"""

#HOST&&PORT: http://127.0.0.1:5000/

import sqlite3
import flask
from flask import Flask
from flask import g
from flask import request
from flask import jsonify
from werkzeug.datastructures import ImmutableMultiDict
import ast

app = Flask(__name__)

DATABASE = 'database.db'

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
    conn = sqlite3.connect('database.db')
    c = conn.cursor()
    print("connected")
    print "INSERT INTO users VALUES('"+dic['phone']+"', '"+dic['phone']+"')"
    c.execute("INSERT INTO users VALUES('"+dic['phone']+"', '"+dic['phone']+"')")
    print("users done")
    c.execute('SELECT COUNT(pid) FROM path')
    count = c.fetchone()[0]
    c.execute("INSERT INTO path VALUES('"+str(count)+"', '"+dic['phone']+"', '"+dic['title']+"', '"+dic['zipCodeList'][0]+"')")
    print("path done")    
    for i in range(0, len(dic['lat'])):
        c.execute("INSERT INTO points VALUES('"+str(count)+"', '"+str(dic['lat'][i])+"', '"+str(dic['lng'][i])+"', '"+str(i)+"')")
    print("points done")
    for i in range(0, len(dic['markerMap'].keys())):
        key = sorted(dic['markerMap'].keys())[i]
        c.execute("INSERT INTO markers VALUES('"+str(count)+"', '"+str(dic['markerMap'][key]['lat'])+"', '"+str(dic['markerMap'][key]['lng'])+"', '"+dic['markerMap'][key]['description']+"', '"+dic['markerMap'][key]['image']+"')")
    print("markers done")
    conn.commit()
    conn.close()
    print("connection closed")
    return request.json

@app.route("/getDetail", methods=["GET", "POST"])
def getDetail():
    if request.method == "GET":
        print "someone got some detail"
        return "You have gotten some detail"

@app.route("/getTitle", methods=["GET", "POST"])
def getTitle():
    if request.method == "GET":
        print "someone got some title"
        return "You have gotten some title"



if __name__ == "__main__":
    app.run(host='0.0.0.0', port=80)
    #app.run(host='0.0.0.0',port="80")