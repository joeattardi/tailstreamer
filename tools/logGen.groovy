pages = ['/home', '/account', '/login', '/logout', '/history', '/contact' ]
statusCodes = [200, 401, 403, 500]
rnd = new Random()

def getIpAddress() {
    return "${rnd.nextInt(256)}.${rnd.nextInt(256)}.${rnd.nextInt(256)}.${rnd.nextInt(256)}"
}

def getPage() {
    return pages.get(rnd.nextInt(pages.size))
}

def getTimestamp() {
    return new Date().format("dd/MMM/yyyy:HH:mm:ss Z")
}

def getStatus() {
    return statusCodes.get(rnd.nextInt(statusCodes.size))
}

def getLine() {
    return "${getIpAddress()} - - [${getTimestamp()}] \"GET ${getPage()} HTTP/1.1\" ${getStatus()} 1234"
}

while (true) {
    sleep rnd.nextInt(500) + 250
    println getLine()
}
