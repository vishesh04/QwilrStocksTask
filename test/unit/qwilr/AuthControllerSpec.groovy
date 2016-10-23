package qwilr

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(AuthController)
class AuthControllerSpec extends Specification {

    void "test googleLogin"() {
        when:
        controller.googleLogin()

        then:
        response.status == 302
    }

    void "test logout"() {
        when:
        controller.logout()

        then:
        session.userId == null
        response.status == 302
    }
}
