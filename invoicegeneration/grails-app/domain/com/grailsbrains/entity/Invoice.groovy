package com.grailsbrains.entity

import grails.databinding.BindingFormat

import javax.management.StandardEmitterMBean

class Invoice {

    String title
    int referenceNumber
    String poNumber
    String dueDate
    String invoiceDate
    String description
    String notes
    String footer
    Vendor vendor
    Client client


    static hasMany = [items:Item]



    static constraints = {
        dueDate(nullable: true)
        invoiceDate(nullable: true)
        description(nullable: true)
        notes(nullable: true)
        footer(nullable: true)
    }

}
