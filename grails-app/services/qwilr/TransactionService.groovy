package qwilr

import grails.transaction.Transactional

@Transactional
class TransactionService {

    def buyStock(Integer quantity, String symbol, User user, String name, requestedPrice) {
        if (quantity < 1) throw new WebException("Please request atleast 1 unit to buy", 403)
        Stock stock = Stock.findBySymbolAndUser(symbol, user)
        if (!stock) stock = new Stock(user: user, name: name, symbol: symbol, quantity: 0)
        // refetch the price
        def unitPrice = StockApi.getQuote(symbol).LastPrice as float
        if (unitPrice != requestedPrice) throw new WebException("Stock price changed to $unitPrice/unit. Please try buying again", 409)
        def cost = unitPrice*quantity
        if (cost <= user.cash) {
            stock.quantity += quantity
            Transaction t = new Transaction(type: TransactionType.BUY, quantity: quantity, unitPrice: unitPrice)
            stock.addToTransactions(t)
            user.cash -= cost
            user.save(flush: true, failOnError: true)
            stock.save(flush: true, failOnError: true)
        } else {
            throw new WebException('Insufficient balance. Please update your cash balance', 403)
        }
    }

    def sellStock(quantity, user, symbol, requestedPrice) {
        if (quantity < 1) throw new WebException("Please request atleast 1 unit to sell", 403)
        Stock stock = Stock.findBySymbolAndUser(symbol, user)
        if (quantity > stock.quantity) throw new WebException("Insufficient stocks", 403)
        // refetch the price
        def unitPrice = StockApi.getQuote(symbol).LastPrice as float
        if (unitPrice != requestedPrice) throw new WebException("Stock price changed to $unitPrice/unit. Please try selling again", 409)
        def cost = unitPrice*quantity
        Transaction t = new Transaction(type: TransactionType.SELL, quantity: quantity, unitPrice: unitPrice)
        stock.quantity -= quantity
        stock.addToTransactions(t)
        user.cash += cost
        user.save(flush: true, failOnError: true)
        stock.save(flush: true, failOnError: true)
    }
}
