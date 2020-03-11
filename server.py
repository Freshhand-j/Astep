#!/usr/bin/python
# -*- coding: UTF-8 -*-
from socket import *

serverIP = '127.0.0.1'
serverPort = 10086

# 创建TCP套接字，使用IPv4协议
serverSocket = socket(AF_INET, SOCK_STREAM) 
# 将TCP套接字绑定到指定端口
serverSocket.bind((serverIP,serverPort)) 
# 最大连接数为1
serverSocket.listen(1) 

print("The server is ready to receive")


while True:
	# 接收到客户连接请求后，建立新的TCP连接套接字
    conn, addr = serverSocket.accept() 
    print('Accept new connection from %s:%s...' % addr)

    # 获取客户发送的字符串
    sentence = conn.recv(1024) 
    print('Server received: %s' % sentence)

    # 将字符串改为大写
    capitalizedSentence = sentence.upper() 

    # 向用户发送修改后的字符串
    conn.send(capitalizedSentence) 
    print('Server sent: %s' % capitalizedSentence)

    # 关闭TCP连接套接字...
    conn.close() 
