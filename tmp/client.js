'use strict';
var socket = require('net').connect(8080)
socket.on('data', console.log.bind(console, 'data: %s'))
setInterval(() => {let d=Math.random();console.log('send :: %s',d),socket.write(d+'\n')}, 1000)
