class UrlMappings {

    static mappings = {
        "500"(controller: "Error")
        "/"(controller: "Home")

        "/auth/google/login"(controller: "Auth") { action = [GET: "googleLogin"] }
        "/auth/google/callback"(controller: "Auth") { action = [GET: "googleCallback"] }
        "/auth/logout"(controller: "Auth") { action = [GET: "logout"] }

        "/api/assets"(controller: "Api") { action = [GET: "getAssets"] }
        "/api/assets/cash"(controller: "Api") { action = [POST: "updateCash"] }
        "/api/assets/buy"(controller: "Api") { action = [POST: "buyStock"] }
        "/api/assets/sell"(controller: "Api") { action = [POST: "sellStock"] }

        "/api/lookup"(controller: "Api") { action = [GET: "getCompanies"] }
        "/api/quote"(controller: "Api") { action = [GET: "getQuote"] }
    }
}
