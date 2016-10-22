package qwilr

class Utils {
    static def getAbsoluteUrl(request) {
        def url =  request.scheme + '://' + request.serverName
        if (!(request.serverPort in [80, 443])) url += ':' + request.serverPort
        return url
    }
}
