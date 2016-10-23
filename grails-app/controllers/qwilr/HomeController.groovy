package qwilr

class HomeController {

    def index() {
        if (session.userId) {
            User user = User.get(session.userId)
            [loggedIn     : true,
             userFirstName: user.firstName]
        } else {
            [loggedIn: false]
        }
    }
}
