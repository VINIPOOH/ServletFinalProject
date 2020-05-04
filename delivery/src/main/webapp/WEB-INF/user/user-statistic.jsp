<%@ include file="../layout/metadata-standart.jsp" %>

<html lang="${param.lang}">
<head>
    <%@ include file="../layout/bootstrap.jsp" %>
    <title><fmt:message key="user_statisticpage.title"/></title>
</head>
<body>
<%@ include file="../layout/header.jsp" %>
<%@ include file="../layout/userheader.jsp" %>


<div name="deliveries" class="container">
    <c:forEach var="bill" items="${requestScope.billsList}">
    <div class="row border border-info mt-2 rounded">
        <p1>номер счета ${bill.id} номер доставки${bill.deliveryId} цена в центах${bill.costInCents} дата оплаты${bill.dateOfPay}${bill.isDeliveryPaid}</p1>
    </div>
    </c:forEach>
</div>
</body>
</html>