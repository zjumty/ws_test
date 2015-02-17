#!/bin/sh
nohup node server.js 2>&1 >> nohup.out & 
echo $! > server.pid