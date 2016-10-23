package qwilr

import grails.converters.JSON

/* Controller to handle exception thrown by all other controllers */

class ErrorController {

    def index() {
        def resp = [:]
        def exception = request.exception.cause
        if (exception instanceof WebException) {
            response.status = exception.responseCode
            resp.error = exception.message
        } else {
            response.status = 500
            resp.error = exception.message
        }
        render resp as JSON
    }
}
