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
        <p1><fmt:message key="user-statistic.bill.number"/> ${bill.id}
            <fmt:message key="user-statistic.delivery.number"/>${bill.deliveryId}
            <fmt:message key="user-statistic.price"/>${bill.costInCents}
            <fmt:message key="user-statistic.pay.date"/>${bill.dateOfPay}</p1>
    </div>
    </c:forEach>
</div>
</body>
</html>