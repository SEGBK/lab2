require('net').createServer((sock) => {
  sock.on('data', console.log.bind(console,'data: %s'))
  setInterval(()=>sock.write(Math.random()+'\n'),1000)
}).listen(8080)
