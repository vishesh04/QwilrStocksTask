package qwilr

import grails.converters.JSON

class ApiController {

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
        if (requestedQuanity < 1) {
            throw new WebException("Please request atleast 1 unit to buy", 403)
        }
        Stock stock = Stock.findBySymbolAndUser(input.Symbol, user)
        if (!stock) {
            stock = new Stock(user: user, name: input.Name, symbol: input.Symbol, quantity: 0)
        }
        // refetch the price
        def unitPrice = StockApi.getQuote(input.Symbol).LastPrice as float
        def requestedPrice = input.LastPrice as float
        if (unitPrice != requestedPrice) {
            throw new WebException("Stock price changed to $unitPrice/unit. Please try buying again", 409)
        }
        def cost = unitPrice*requestedQuanity
        if (cost <= user.cash) {
            stock.quantity += requestedQuanity
            Transaction t = new Transaction(type: TransactionType.BUY, quantity: requestedQuanity, unitPrice: unitPrice)
            stock.addToTransactions(t)
            user.cash -= cost
            user.save(flush: true, failOnError: true)
            stock.save(flush: true, failOnError: true)
        } else {
            throw new WebException('Insufficient balance. Please update your cash balance', 403)
        }
        def resp = [:]
        render resp as JSON
    }

    def sellStock() {
        def input = request.JSON
        def user = User.get(session.userId)
        def requestedQuanity = input.quantityToSell as Integer
        if (requestedQuanity < 1) {
            throw new WebException("Please request atleast 1 unit to sell", 403)
        }
        Stock stock = Stock.findBySymbolAndUser(input.symbol, user)
        if (requestedQuanity > stock.quantity) {
            throw new WebException("Insufficient stocks", 403)
        }
        def unitPrice = StockApi.getQuote(input.symbol).LastPrice as float
        def requestedPrice = input.LastPrice as float
        if (unitPrice != requestedPrice) {
            throw new WebException("Stock price changed to $unitPrice/unit. Please try selling again", 409)
        }
        def cost = unitPrice*requestedQuanity
        Transaction t = new Transaction(type: TransactionType.SELL, quantity: requestedQuanity, unitPrice: unitPrice)
        stock.quantity -= requestedQuanity
        stock.addToTransactions(t)
        user.cash += cost
        user.save(flush: true, failOnError: true)
        stock.save(flush: true, failOnError: true)
        def resp = [:]
        render resp as JSON
    }
}
