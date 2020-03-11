#!/usr/bin/python
# -*- coding: UTF-8 -*-
from socket import *
import time

serverIP = '127.0.0.1'
serverPort = 10086

# 创建TCP套接字，使用IPv4协议
s = socket(AF_INET, SOCK_STREAM) 
s.connect((serverIP,serverPort))

# 向服务端发送数据
s.send("hello")

#接收服务端发来的数据并打印
print s.recv(1024)

# 等待5s 防止python进程结束
# time.sleep(5)

# 关闭socket
s.close()

# 等待5s 防止python进程结束
# time.sleep(5)


