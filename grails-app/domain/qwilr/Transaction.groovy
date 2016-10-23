package qwilr

class Transaction {
    TransactionType type
    Integer quantity
    Float unitPrice

    Date dateCreated
    Date lastUpdated

    static belongsTo = [stock: Stock]
}
