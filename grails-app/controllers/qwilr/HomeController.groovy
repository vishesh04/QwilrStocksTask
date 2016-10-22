package qwilr

class HomeController {

    def index() {
        if (session.userId) {
            [loggedIn     : true,
             userFirstName: 'Vishesh']
        } else {
            [loggedIn: false]
        }
    }
}
