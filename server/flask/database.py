# -*- coding: utf-8 -*-
"""
Created on Sat Nov 12 20:46:17 2016

@author: Dave Ho
"""

import sqlite3

conn = sqlite3.connect('database.db')
c = conn.cursor()

c.execute('''CREATE TABLE users 
            (name char primary key not null unique, phone integer not null)''')

c.execute('''CREATE TABLE path
            (pid integer primary key not null unique, name char not null, title char not null, zip char not null)''')
            
c.execute('''CREATE TABLE points
            (pid integer not null, lat integer not null, lng integer not null)''')
            
c.execute('''CREATE TABLE markers
            (pid integer not null, lat integer not null, lng integer not null, description char not null, image char not null)''')