package qwilr

class Stock {
    String name
    String symbol
    Integer quantity = 0

    Date dateCreated
    Date lastUpdated

    static hasMany = [transactions: Transaction]
    static belongsTo = [user:User]

    static constraints = {
    }
}
