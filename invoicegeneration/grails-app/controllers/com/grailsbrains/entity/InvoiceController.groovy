package com.grailsbrains.entity

import com.grailsbrains.entity.InvoiceService
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import javafx.scene.Parent

import javax.persistence.Id

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class InvoiceController {

    def springSecurityService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    @Secured(['ROLE_ADMIN', 'ROLE_USER'])
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Invoice.list(params), model: [invoiceCount: Invoice.count()]
    }

    def displayLogo = {
        println("sl"+params.organizationName)
        def invoice = Invoice.get(params.id)
        response.contentType = "image/jpeg"
        response.contentLength = invoice?.logo.length
        response.outputStream.write(invoice?.logo)
    }

    @Secured(['ROLE_ADMIN', 'ROLE_USER'])
    def aftersave(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def singleInvoice = Invoice.findById(params.id)
        println(singleInvoice.poNumber)

        // def vendorList= Vendor.list().toString()
        /* for (Vendor vendor: vendorList) {
           // Byte[] logos = vendor.getLogo()*/
        def s = (singleInvoice.vendor.logo)
        println "jjjjjjjj" + s
        /*       response.contentType = "image/jpg"
            response.contentLength = s?.logo.length
            response.outputStream.write(s?.logo)
            response.outputStream.flush()*/


        // }
        def user = springSecurityService.currentUser
        def a = session.getAttribute("params")
        def format = params.format
        def itemList = singleInvoice.items
        for (Item item : itemList) {
            println("@@@@@@@@@@@@@@" + format)
            println("@@@@@@@@@@@@@@" + item.quantity)

            /* Invoice invoice
        println "==========================  "  + invoice.getItems()*/
            //render  view :"demopdf", model:[invoices:singleInvoice, invoiceCount: Invoice.count()]
            if (params.format == "pdf") {
                render(filename: "Invoice.pdf",
                        view: "pdfdesign",
                        model: [invoices: singleInvoice, user: user, item: itemList, invoiceCount: Invoice.count()],
                        marginLeft: 20,
                        marginTop: 20,
                        marginBottom: 25,
                        marginRight: 20,
                        headerSpacing: 15)
            }
            else
                render view: "pdfdesign",
                        model: [invoices: singleInvoice, user: user, item: itemList, invoiceCount: Invoice.count()]
        }
    }


        @Secured(['ROLE_ADMIN', 'ROLE_USER'])
    def showtemplate() {

        render view: 'template'
    }

    @Secured(['ROLE_ADMIN', 'ROLE_USER'])
    def showinvoice() {
        println("parrrrrrms" + params.fileupload.getBytes())
        Byte[] logo=params.fileupload.getBytes()
        // def invoice = Invoice.getId(params.id)
        //println("ttttttttttttttttttttttttt"+invoice )
        def user = springSecurityService.currentUser
        def vendor = Vendor.findByUser(user)
      println("jjjjjjjjjjjjjj"+logo)
        def a=vendor.setLogo(logo)
        vendor.save(flush: true)
        println("vendor " + vendor)
        println("inside invoice controller " + user.organizationName)
        render view: 'invoice', model: [user: user, vendor: vendor,a:a]
    }


    @Secured(['ROLE_ADMIN', 'ROLE_USER'])
    def invoicecreate() {
        def user = springSecurityService.currentUser
        def vendor = Vendor.findByUser(user)
        println("params" + params.itemName)
        def taxId = params.invoiceDate
        def invoice = new Invoice(params)
        Tax tax = Tax.findByTaxName(params.taxName)
        def items = new Item(params)
        items.setTax(tax)
        Client client = Client.findByOrganizationName(params.organizationName)
        println("organizationName" + params.organizationName)
        invoice.setClient(client)
        invoice.setVendor(vendor)
        invoice.addToItems(items)
        invoice.validate()
        def invoiceId = invoice.save(flush: true, failOnError: true)
        def inid = invoiceId.getId()
        println("iiiiiiiiiiiiiiiiid    " +invoiceId.vendor )
        redirect(controller: "invoice", action: "aftersave", params: [id: inid])
    }


    @Secured(['ROLE_ADMIN', 'ROLE_USER'])
    def invoicelist() {
        def invoices = Invoice.list()
        render(view: "invoicelist", model: [invoices: invoices] )
    }

    def show(Invoice invoice) {
        respond invoice
    }

    def create() {
        respond new Invoice(params)
    }

    @Transactional
    def save(Invoice invoice) {
        if (invoice == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (invoice.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond invoice.errors, view: 'create'
            return
        }

        invoice.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'invoice.label', default: 'Invoice'), invoice.id])
                redirect invoice
            }
            '*' { respond invoice, [status: CREATED] }
        }
    }

    def edit(Invoice invoice) {
        respond invoice
    }

    @Transactional
    def update(Invoice invoice) {
        if (invoice == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (invoice.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond invoice.errors, view: 'edit'
            return
        }

        invoice.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'invoice.label', default: 'Invoice'), invoice.id])
                redirect invoice
            }
            '*' { respond invoice, [status: OK] }
        }
    }


    @Transactional
    def delete(Invoice invoice) {

        if (invoice == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        invoice.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'invoice.label', default: 'Invoice'), invoice.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }


    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'invoice.label', default: 'Invoice'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
    @Transactional
    def list() {
        [invoice: Invoice.list()]
    }
}