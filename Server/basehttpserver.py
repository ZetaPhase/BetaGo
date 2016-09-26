# -*- coding: utf-8 -*-
"""
Created on Sat Jul 23 10:10:48 2016

@author: Dave Ho
"""

import sys
import time
import BaseHTTPServer
import cgi
import json
from urlparse import urlparse
import os.path

HOST_NAME = '192.168.1.68'
PORT_NUMBER = 80

class MyHandler(BaseHTTPServer.BaseHTTPRequestHandler): # set up handler


    def do_HEAD(self):
        self.send_response(200)
        self.send_header("Content-type", "text/html")
        self.end_headers()
        
    def do_GET(self):
        #file = open('myfile.dat', 'w+')
        print self.path
        query = urlparse(self.path).query
        path = urlparse(self.path).path
        query_components = dict(qc.split("=") for qc in query.split("&"))
        print(query_components)
        try:
            print self.dictionary
            print self.zipDictionary
        except AttributeError: 
            self.dictionary = {}
            self.zipDictionary = {}  
            with open("records.txt", 'r') as myfile:
                data=myfile.read()
                array = data.splitlines()
                for string in array:
                    self.dictionary[json.loads(string)["phone"]+json.loads(string)["title"]]=string
                    for zipCode in json.loads(string)["zipCodeList"]:
                        if self.zipDictionary.has_key(zipCode):
                            self.zipDictionary[zipCode] = self.zipDictionary[zipCode] + [json.loads(string)["phone"]+json.loads(string)["title"]]
                        else:
                            self.zipDictionary[zipCode] = [json.loads(string)["phone"]+json.loads(string)["title"]]
        if path == '/getDetail':
            idName = query_components['id']
            self.send_response(200)
            self.send_header("Content-type", "text/html")
            self.end_headers()
            print self.dictionary[idName]
            self.wfile.write(self.dictionary[idName])
        elif path == '/getTitle':
            zipCode = query_components['zipCode']
            resultString = ""
            if self.zipDictionary.has_key(zipCode):
                for idString in self.zipDictionary[zipCode]:
                    resultString += idString + "\n"
                    #resultString += self.dictionary[idString] + "\n"
            else:
                self.zipDictionary[zipCode] = []
            self.send_response(200)
            self.send_header("Content-type", "text/html")
            self.end_headers()
            self.wfile.write(resultString)
        '''
        print self.zipDictionary
        print self.dictionary
        self.send_response(200)
        self.send_header("Content-type", "text/html")
        self.end_headers()
        self.wfile.write(resultString)
        '''
        
    def do_POST(self):
        #print(self.headers['Content-Type'])
        #print(type(self.headers.getheader('content-length')))
        #print(self.rfile.read(int(self.headers.getheader('content-length'))))
        json_string = self.rfile.read(int(self.headers.getheader('content-length')))
        d = json.loads(json_string)
        try:
            print self.dictionary
            print self.zipDictionary
        except AttributeError: 
            self.dictionary = {}
            self.zipDictionary = {}  
            with open("records.txt", 'r') as myfile:
                data=myfile.read()
                array = data.splitlines()
                for string in array:
                    self.dictionary[json.loads(string)["phone"]+json.loads(string)["title"]]=string
                    for zipCode in json.loads(string)["zipCodeList"]:
                        if self.zipDictionary.has_key(zipCode):
                            self.zipDictionary[zipCode] = self.zipDictionary[zipCode] + [json.loads(string)["phone"]+json.loads(string)["title"]]
                        else:
                            self.zipDictionary[zipCode] = [json.loads(string)["phone"]+json.loads(string)["title"]]
        if not self.dictionary.has_key(d["phone"]+d["title"]):
            self.dictionary[d["phone"]+d["title"]]=json_string
        for string in d["zipCodeList"]:
            if self.zipDictionary.has_key(string):
                if not self.zipDictionary[string].__contains__(d["phone"]+d["title"]):
                    self.zipDictionary[string] = self.zipDictionary[string] + [d["phone"]+d["title"]]
            else:
                self.zipDictionary[string] = [d["phone"]+d["title"]]
        if not os.path.isfile("records.txt"):
            file = open("records.txt", "w+")
        else:
            file = open("records.txt", "a")
        file.write(json_string+"\n")
        file.close()
        self.send_response(200)
        self.end_headers()
        print('Attempting to write')
        print("Somebody post me.")
        self.wfile.write("successful")

if __name__ == '__main__':
    server_class = BaseHTTPServer.HTTPServer
    httpd = server_class((HOST_NAME, PORT_NUMBER), MyHandler)
    print time.asctime(), "Server Starts - %s:%s" % (HOST_NAME, PORT_NUMBER)
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        pass
    httpd.server_close()
    print time.asctime(), "Server Stops - %s:%s" % (HOST_NAME, PORT_NUMBER)