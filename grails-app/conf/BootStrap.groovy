import grails.converters.JSON
import qwilr.Stock
import qwilr.Transaction

class BootStrap {

    def init = { servletContext ->
        JSON.registerObjectMarshaller(Stock) { Stock stock ->
            def m = [:]
            m.id = stock.id
            m.name = stock.name
            m.symbol = stock.symbol
            m.quantity = stock.quantity
            m.transactions = stock.transactions
            return m
        }
        JSON.registerObjectMarshaller(Transaction) { Transaction t ->
            def m = [:]
            m.id = t.id
            m.quantity = t.quantity
            m.unitPrice = t.unitPrice
            m.type = t.type.toString()
            m.time = t.dateCreated.format("dd/mm/yyy 'at' hh:mm a")
            return m
        }
    }
    def destroy = {
    }
}
