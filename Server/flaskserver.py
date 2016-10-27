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


app = Flask(__name__)

DATABASE = 'database.db'

@app.route("/", methods=["GET", "POST"])
def hello():
    if request.method == "GET":
        return "Hello World!"
    elif request.method == "POST":
        #json_string = self.rfile.read(int(self.headers.getheader('content-length')))
        json_string = request.data   
        print "someone posted something"
        print json_string
        return json_string

@app.route("/getDetail", methods=["GET", "POST"])
def getDetail():
    if request.method == "GET":
        return "You have gotten some detail"

@app.route("/getTitle", methods=["GET", "POST"])
def getTitle():
    if request.method == "GET":
        return "You have gotten some title"



if __name__ == "__main__":
    app.run()