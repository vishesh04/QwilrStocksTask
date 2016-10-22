package qwilr

class User {
    String firstName
    String lastName
    String emailId

    Date dateCreated
    Date lastUpdated

    static hasMany = [stocks: Stock]

    static constraints = {
        lastName nullable: false
    }
}
