class UrlMappings {

    static mappings = {
        "/"(controller: "Home")

        "/auth/google/login"    (controller: "Auth"){ action = [GET: "googleLogin"] }
        "/auth/google/callback" (controller: "Auth"){ action = [GET: "googleCallback"] }
        "/auth/logout"          (controller: "Auth"){ action = [GET: "logout"] }
    }
}
