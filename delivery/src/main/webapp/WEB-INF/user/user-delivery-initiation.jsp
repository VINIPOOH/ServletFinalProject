<%@ include file="../layout/metadata-standart.jsp" %>

<html lang="${param.lang}">
<head>
    <%@ include file="../layout/bootstrap.jsp" %>
    <title><fmt:message key="homepage.title"/></title>
</head>
<body>
<%@ include file="../layout/header.jsp" %>
<%@ include file="../layout/userheader.jsp" %>


<div th:include="~{layout/header.html::header}"></div>
<div th:include="~{layout/header.html::userHeader}"></div>
<div class="container">
    <div class="row">
        <div class="col-md-6">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-heading"><fmt:message key="user-delivery-initiation.form.name"/></h3>
                </div>
                <div class="panel-body">
                    <form id="form" class="form" method="post" action="${pageContext.request.contextPath}/user/user-delivery-initiation">
                        <c:if test="${inputHasErrors}">
                            <div class="alert alert-danger" role="alert">
                                <p><fmt:message key="user-profile.form.wrong"/></p>
                            </div>
                        </c:if>
                        <c:if test="${unsupportableWeightOrWay}">
                            <div class="alert alert-danger" role="alert">
                                <p><fmt:message key="registrationpage.form.weight.wrong"/></p>
                            </div>
                        </c:if>

                        <div class="form-group">
                            <input type="text" class="form-control" id="deliveryWeight" name="deliveryWeight"
                                   placeholder=<fmt:message key="homepage.form.weight"/>>
                        </div>
                        <label><fmt:message key="homepage.form.label.locality_sand"/></label>
                        <select id="localitySandID" name="localitySandID" form="form" class="form-control">
                            <c:forEach var="locality" items="${requestScope.localityList}">
                                <option value="${locality.id}" id="localitySandID"
                                        name="localitySandID">${locality.name}</option>
                            </c:forEach>
                        </select>
                        <label><fmt:message key="homepage.form.label.locality_get"/></label>
                        <select id="localityGetID" name="localityGetID" form="form" class="form-control">
                            <c:forEach var="locality" items="${requestScope.localityList}">
                                <option value="${locality.id}" id="localityGetID"
                                        name="localityGetID">${locality.name}</option>
                            </c:forEach>
                        </select>
                        <p1></p1>
                        <label><fmt:message key="homepage.form.label.aderesee.email"/></label>
                        <div class="form-group">
                            <input type="text" class="form-control" id="addresseeEmail" name="addresseeEmail">
                        </div>
                        <button class="btn btn-success" type="submit"><fmt:message key="user-delivery-initiation.button.massege"/></button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>