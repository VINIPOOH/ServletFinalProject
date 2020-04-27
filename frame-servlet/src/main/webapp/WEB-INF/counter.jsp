<%@ include file="layout/metadata-standart.jsp" %>

<html lang="${param.lang}">
<head>
    <%@ include file="layout/bootstrap.jsp" %>
    <title ><fmt:message key="title.counter"/></title>
</head>
<body>
<%@ include file="layout/header.jsp" %>
<div th:include="~{layout/header.jsp::header}"></div>
<div class="container">
    <div class="row">
        <div class="col-md-6">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-heading"><fmt:message key="homepage.form.header"/></h3>
                </div>
                <div class="panel-body">
                    <form  id="form" class="form" method="post" action="${pageContext.request.contextPath}/home}">
                        <c:if test="incorrectWeightInput">
                            <div class="alert alert-danger" role="alert" >
                            <p><fmt:message key="homepage.form.weight.incorect"/></p>
                        </div>
                        </c:if>
                        <c:if test="unsupportableWeightFactorException">
                        <div class="alert alert-danger" role="alert">
                            <p><fmt:message key="registrationpage.form.weight.wrong"/></p>
                        </div>
                        </c:if>
                        <c:if test="noSuchWayException">
                        <div class="alert alert-danger" role="alert">
                            <p><fmt:message key="registrationpage.form.way.wrong"/></p>
                        </div>
                        </c:if>

                        <div class="form-group" >
                            <input type="number" class="form-control" id="deliveryWeight" name="deliveryWeight" placeholder=<fmt:message key="homepage.form.weight"/>>
                        </div>
                        <label><fmt:message key="homepage.form.label.locality_sand"/></label>
                        <select form="form" class="form-control">
                            <c:forEach var="locality" items="${sessionScope.localityList}">
                            <option  value="${locality.id}" id= "localitySandID" name="localitySandID">${locality.nameRu}</option>
                            </c:forEach>
                        </select>
                        <label><fmt:message key="homepage.form.label.locality_get"/></label>
                        <select th:field="*{localityGetID}" form="form" class="form-control">
                            <c:forEach var="locality" items="${sessionScope.localityList}">
                            <option value="${locality.id}" id="localityGetID" name="localityGetID">${locality.nameRu}</option>
                            </c:forEach>
                        </select>
                        <p1></p1>
                        <button class="btn btn-success" type="submit"><fmt:message key="homepage.form.button"/></button>
                    </form>
                </div>
            </div>
        </div>
        <c:if test="${sessionScope.deliveryCostAndTimeDto!=null}">
        <div class="col-md-6 container-fluid">
            <table class="table">
                <tbody>
                <tr>
                    <td><span><fmt:message key="homepage.paragraf.price"/></span></td>
                    <td><span>${sessionScope.deliveryCostAndTimeDto.costInCents}</span></td>
                </tr>
                <tr>
                    <td><span><fmt:message key="homepage.paragraf.time"/></span></td>
                    <td><span>${sessionScope.deliveryCostAndTimeDto.timeOnWayInHours}</span></td>
                </tr>
                </tbody>
            </table>

        </div>
        </c:if>
    </div>
</div>
</body>
</html>