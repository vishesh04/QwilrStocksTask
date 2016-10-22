package qwilr

import grails.util.Holders
import groovyx.net.http.RESTClient
import static groovyx.net.http.ContentType.URLENC

class Google {
    def static grailsApplication = Holders.grailsApplication
    private static final String CLIENT_ID     = grailsApplication.config.grails.auth.google.clientId
    private static final String CLIENT_SECRET = grailsApplication.config.grails.auth.google.clientSecret

    private static final String BASE_LOGIN_URL = 'https://accounts.google.com/o/oauth2/v2/auth'
    private static final String API_BASE_URL = 'https://www.googleapis.com'

    /**
     * returns Google OAuth URL with email and profile scope
     */
    static def getLoginUrl(redirectUrl) {
        def params = [
                client_id: CLIENT_ID,
                redirect_uri: redirectUrl,
                response_type: 'code',
                scope: 'email profile'
        ]
        return BASE_LOGIN_URL + '?' + params.collect { k, v -> "$k=$v" }.join('&')
    }

    /* Exchanges OAuth temp code for access token*/
    static def exchangeCode(String code, redirectUrl) {
        def restClient = new RESTClient(API_BASE_URL)
        def resp = restClient.post(
                path: '/oauth2/v4/token',
                body: [client_id   : CLIENT_ID, client_secret: CLIENT_SECRET,
                       redirect_uri: redirectUrl, code: code, grant_type: 'authorization_code'],
                requestContentType: URLENC)
        return resp.data
    }

    /* Gets User profile */
    static def getUserProfile(String accessToken) {
        def restClient = new RESTClient(API_BASE_URL)
        restClient.headers."Authorization" = "Bearer $accessToken"
        def resp = restClient.get(path: '/plus/v1/people/me',
                requestContentType: URLENC)
        if (resp.data.emails) {
            def email = resp.data.emails[0].value
            def profile = [email    : email,
                           firstName: resp.data.name?.givenName ?: email.split("@")[0],
                           lastName : resp.data.name?.familyName ?: '']
            return profile
        }
    }
}
