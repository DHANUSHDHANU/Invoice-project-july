package com.grailsbrains.entity

class Item {

    String itemName
    int quantity
    int price
    int amount
    int subTotal
    String itemdescription
    Tax tax
    static belongsTo = Invoice

    static constraints = {
        amount(nullable:true, blank:true)
        subTotal(nullable:true, blank:false)
        itemdescription(nullable:true, blank:true)


    }
}
