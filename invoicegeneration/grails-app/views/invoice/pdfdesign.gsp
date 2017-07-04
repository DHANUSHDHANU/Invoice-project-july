<%@ page import="com.grailsbrains.entity.Item" %>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

<!-- jQuery library -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

<!-- Latest compiled JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<style>
.bigHr {
    background-color: royalblue !important;
    color: royalblue!important;
    height: 25px !important;
    width: 100% !important;
}
.s{
    background-color: blue;
    color: black;
    height: 1px !important;
    width: 100% !important;
}

</style>

<html>
<head>

</head>


<body>
<div class="col-lg-offset-1 col-lg-10 col-sm-offset-1 col-md-10 col-md-offset-1 col-sm-10  ">
<div class="container">
    <hr class="bigHr">
</div>
<g:form>
    <g:each in="${invoices}" status="i" var="invoiceInstance">

    <div class="row container-fluid">
        <div class="col-sm-6 col-md-6 col-lg-6 col-xs-6 " >
           <h2>${user.organizationName}</h2>
                <h4><%= invoiceInstance.vendor.addresses[0].country %>
            <%= invoiceInstance.vendor.addresses[0].state%>
            <%= invoiceInstance.vendor.addresses[0].city%></h4>
           <h4><%= invoiceInstance.vendor.addresses[0].street%>
            <%= invoiceInstance.vendor.addresses[0].pincode%></h4>

        </div>
        <div class="col-sm-3 col-md-3 col-lg-3 col-xs-3 " align="right">
            <span class="property-value" aria-labelledby="logo-label">
                <img src="http://localhost:8080/invoice"${createLink(action:'displayLogo', id:invoiceInstance?.id)}" />

            </span>
           %{-- <img src='${createLink(controller: "invoiceController", action: "showinvoice")}' width="120px" height="120px"/>--}%
        </div>
    </div>
    <br>
    <div class="row">
       %{-- <div class="col-sm-6 col-md-6 col-lg-6 col-xl-6"><h3>BILL TO</h3>
                <h4><%= invoiceInstance.client.addresses[0].country %>
            <%= invoiceInstance.client.addresses[0].state%>
                <%= invoiceInstance.client.addresses[0].city%></h4>
                <h4><%= invoiceInstance.client.addresses[0].street%>
                    <%= invoiceInstance.client.addresses[0].pincode%></h4>

        </div>--}%
        <table style="width:100%">
            <tr>
                <th><h4>BILL TO</h4></th>
                <td><h4>Invoice #</h4> <td><h4>${fieldValue(bean:invoiceInstance ,field: "referenceNumber") }</h4></td>

            </td>
            </tr>
            <tr>
                <td> <h4><%= invoiceInstance.client.addresses[0].country %>
                <%= invoiceInstance.client.addresses[0].state%>
                    <%= invoiceInstance.client.addresses[0].city%></h4>
                    <h4><%= invoiceInstance.client.addresses[0].street%>
                        <%= invoiceInstance.client.addresses[0].pincode%></h4></td></td>
                <td><h4>Invoice Date</h4> <td> <h4> ${fieldValue(bean:invoiceInstance ,field: "invoiceDate") }</h4></td>
            </td>

            </tr>
            <tr>
                <td> <td> <h4>P.O#</h4> <td> <h4> ${fieldValue(bean:invoiceInstance ,field: "poNumber") }</h4></td>
            </td>
                <td></td>
            </tr>
            <tr>
                <td></td><td><h4>Due Date</h4><td> <h4> ${fieldValue(bean:invoiceInstance ,field: "dueDate") }</h4></td>
            </td>
            </tr>

        </table>

    <div><hr class="s"></div>
    <div class="row">
        <div class="col-sm-6 col-md-6 col-lg-6 col-xs-6 " align="left"> <h2>INVOICE TOTAL</h2></div>
        <div class="col-sm-6 col-md-6 col-lg-6 col-xs-6 " align="center"> <h1>$</h1></div>
    </div>
    <div><hr class="s"></div>
    <div class="row">

        <div class="col-sm-2 col-md-2 col-lg-2 col-xs-2 "><h4>ITEM</h4></div>
        <div class="col-sm-2 col-md-2 col-lg-2 col-xs-2 "><h4 >DESCREPTION</h4></div>
        <div class="col-sm-2 col-md-2 col-lg-2 col-xs-2 "><h4>UNIT PRICE</h4></div>
        <div class="col-sm-3 col-md-3 col-lg-3 col-xs-3 "><h4 >QTY</h4></div>
        <div class="col-sm-3 col-md-3 col-lg-3 col-xs-3  "><h4>AMOUNT</h4></div>
        <%if(item!=null){
            for(com.grailsbrains.entity.Item item0: item) {%>
        <div class="col-sm-2 col-md-2 col-lg-2 col-xs-2 "><h5><%=item0.getItemName()%></h5></div>
        <div class="col-sm-2 col-md-2 col-lg-2 col-xs-2 "><h5 ><%=item0.getItemdescription()%></h5></div>
        <div class="col-sm-2 col-md-2 col-lg-2 col-xs-2 "><h5><%=item0.getPrice()%></h5></div>
        <div class="col-sm-3 col-md-3 col-lg-3 col-xs-3 "><h5 ><%=item0.getQuantity()%></h5></div>
        <div class="col-sm-3 col-md-3 col-lg-3 col-xs-3 "><h5 ><%=item0.getAmount()%></h5></div>

        <%}}%>
    </div>
    </g:each>
</g:form>
    <br>
    <div class=" ">
        <footer>
            <h4> TERMS AND CONDITION</h4>
        </footer>
    </div>
<g:link controller="invoice" action="aftersave" > <a href=" ${request.requestURI}.pdf" >Generate Pdf</a></g:link>

    <hr class="bigHr">
</div>

</body>
</html>