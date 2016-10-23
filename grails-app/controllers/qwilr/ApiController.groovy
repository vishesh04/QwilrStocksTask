package qwilr

import grails.converters.JSON

class ApiController {

    def beforeInterceptor = [action: this.&auth]

    def auth() {
        if (!session.userId) throw new WebException("User not logged in", 401)
    }

    def transactionService

    def getAssets() {
        def user = User.get(session.userId)
        def stocks = user.stocks
        def resp = [
                cash: user.cash?.round(2),
                stocks: stocks.sort{it.lastUpdated}.reverse()
        ]
        render resp as JSON
    }

    def updateCash() {
        def cash = request.JSON.cash as Float
        if (cash < 0) throw new WebException('Invalid value for cash', 400)
        def user = User.get(session.userId)
        user.cash = cash
        user.save(flush: true, failOnError: true)
        def resp = [success: true, cash: user.cash]
        render resp as JSON
    }

    def getCompanies(String query) {
        def resp = StockApi.lookup(query)
        render resp as JSON
    }

    def getQuote(String symbol) {
        def resp = StockApi.getQuote(symbol)
        render resp as JSON
    }

    def buyStock() {
        def input = request.JSON
        def user = User.get(session.userId)
        def requestedQuanity = input.quantity as Integer
        def requestedPrice = input.LastPrice as float
        def stockName = input.Name
        if (!stockName) throw new WebException("Please provide stock name", 400)
        transactionService.buyStock(requestedQuanity, input.Symbol, user, stockName, requestedPrice)
        def resp = [success: true]
        render resp as JSON
    }

    def sellStock() {
        def input = request.JSON
        def user = User.get(session.userId)
        def requestedQuanity = input.quantityToSell as Integer
        def requestedPrice = input.LastPrice as float
        transactionService.sellStock(requestedQuanity, user, input.symbol, requestedPrice)
        def resp = [success: true]
        render resp as JSON
    }
}
