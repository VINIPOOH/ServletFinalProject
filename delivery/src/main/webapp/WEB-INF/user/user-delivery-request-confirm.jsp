<%@ include file="../layout/metadata-standart.jsp" %>

<html lang="${param.lang}">
<head>
    <%@ include file="../layout/bootstrap.jsp" %>
    <title><fmt:message key="user_statisticpage.title"/></title>
</head>
<body>
<%@ include file="../layout/header.jsp" %>
<%@ include file="../layout/userheader.jsp" %>

<div name="deliveries-which-wait-getting" class="container">
    <c:forEach var="billToPay" items="${requestScope.BillInfoToPay}">
    <div class="row border border-info mt-2 rounded">
        <div class="card-body col-md-3">
            <h5 class="card-title"><fmt:message key="user-delivery-request-confirm.card.title"/>${billToPay.addreeserEmail}</h5>
        </div>
        <div class="col-md-4 mt-3">
            <p1 class="list-group-item"><fmt:message key="user-delivery-request-confirm.card.list.from.city"/>${billToPay.localitySandName}</p1>
        </div>
        <div class="col-md-3 mt-3">
            <p1 class="list-group-item"><fmt:message key="user-delivery-request-confirm.card.list.to.city"/>${billToPay.localityGetName}</p1>
        </div>
        <div class="col-md-3 mt-3">
            <p1 class="list-group-item"><fmt:message key="user-delivery-request-confirm.card.timeondelivery"/>${billToPay.weight}</p1>
        </div>
        <div class="col-md-3 mt-3">
            <p1 class="list-group-item"><fmt:message key="user-delivery-request-confirm.card.priceondelivery"/>${billToPay.price}</p1>
        </div>

        <div class="card-body col-md-2">
            <form class="form" method="post" action="${pageContext.request.contextPath}/user/user-delivery-request-confirm">
                <div class="form-group">
                <input  id="Id" name="Id" value="${billToPay.billId}" type="hidden">
                <button class="btn btn-success" type="submit"><fmt:message key="user-delivery-request-confirm.button.text"/></button>
                </div>
            </form>
        </div>
    </div>
    </c:forEach>
</div>
</body>
</html>