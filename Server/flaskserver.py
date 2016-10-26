# -*- coding: utf-8 -*-
"""
Created on Tue Oct 25 14:49:32 2016

@author: Dave Ho
"""

import sqlite3
from flask import Flask
from flask import g

app = Flask(__name__)

DATABASE = 'database.db'

@app.route("/")
def hello():
    return "Hello World!"

if __name__ == "__main__":
    app.run()