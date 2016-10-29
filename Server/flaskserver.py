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
#from werkzeug.datastructures import ImmutableMultiDict
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
    #print request.data
    #print jsonify(request.get_json(force=True))
    #print (request)
    #print (str(request.form))
    #print (request.form)
    dickeys = request.form.keys()
    dic = ast.literal_eval(dickeys[0])
    print(dic)
    #dic = dict(request.form) 
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