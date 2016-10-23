package qwilr

class AuthController {

    final static String googleCallbackUri = '/auth/google/callback'

    /**
     * Redirects to google OAuth page
     */
    def googleLogin() {
        redirect(url: Google.getLoginUrl(Utils.getAbsoluteUrl(request) + googleCallbackUri))
    }

    /**
     * Exchanges oauth code for access token,
     * Gets user's profile info using access token,
     * Creates the user if one doesn't exists with received emailId
     * Initializes user session
     * @param code - OAuth code, received after user authorizes the app
     * @return
     */
    def googleCallback(String code) {
        if (code) {
            def exchangeCodeResp = Google.exchangeCode(code, Utils.getAbsoluteUrl(request) + googleCallbackUri)
            def userProfile = Google.getUserProfile(exchangeCodeResp.access_token)
            // create user if not exists
            def user = User.findByEmailId(userProfile.email)
            if (!user) {
                user = new User(firstName: userProfile.firstName,
                        lastName: userProfile.lastName, emailId: userProfile.email, cash: 0)
                user.save(flush: true, failOnError: true)
            }
            session.userId = user.id
        }
        redirect(uri: "/")
    }

    def logout() {
        session.invalidate()
        redirect(uri: "/")
    }
}
