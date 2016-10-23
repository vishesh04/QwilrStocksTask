package qwilr

class User {
    String firstName
    String lastName
    String emailId
    Float cash

    Date dateCreated
    Date lastUpdated

    static hasMany = [stocks: Stock]
}
