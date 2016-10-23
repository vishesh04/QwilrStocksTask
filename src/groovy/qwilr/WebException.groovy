package qwilr

import org.codehaus.groovy.grails.exceptions.GrailsException

class WebException extends GrailsException {
    int responseCode = 500 // response code to send for this exception

    public WebException() {}

    public WebException(String message) {
        super(message);
    }

    public WebException(String message, int responseCode) {
        super(message)
        this.responseCode = responseCode
    }
}