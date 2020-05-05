<%@ include file="layout/metadata-standart.jsp" %>
<html lang="${param.lang}">
<head>
    <%@ include file="layout/bootstrap.jsp" %>
    <div th:include="~{layout/header.html::head_base_data}"></div>
    <title ><fmt:message key="title.loginpage"/></title>
</head>
<body>
<%@ include file="layout/header.jsp" %>
<div class="container">
    <div class="row">
        <div class="col-md-6 col-md-offset-3">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-heading"><fmt:message key="loginpage.form.header"/></h3>
                </div>
                <div class="panel-body">
                    <form class="form" method="post" action="${pageContext.request.contextPath}/login">
                        <c:if test="${inputHasErrors}">
                        <div class="alert alert-danger" role="alert">
                            <p><fmt:message key="loginpage.form.wrong"/></p>
                        </div>
                        </c:if>
                        <div class="form-group">
                            <input type="text" class="form-control" id="username" name="username" >
                        </div>
                        <div class="form-group">
                            <input type="password" class="form-control" id="password" name="password">
                        </div>
                        <button class="btn btn-success" type="submit"><fmt:message key="lofinpage.button.login"/></button>
                        <a class="btn"  align="left" href="${pageContext.request.contextPath}/registration"><fmt:message key="lofinpage.button.gotoRegistration"/></a>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>