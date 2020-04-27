<%@ include file="layout/metadata-standart.jsp" %>

<html lang="${param.lang}">
<head>
    <%@ include file="layout/bootstrap.jsp" %>
    <title ><fmt:message key="title.registrationpage"/></title>
</head>
<body>
<%@ include file="layout/header.jsp" %>
<div class="container">
    <div class="row">
        <div class="col-md-6 col-md-offset-3">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-heading"><fmt:message key="registrationpage.form.header"/></h3>
                </div>
                <div class="panel-body">
                    <form class="form" method="post" action="${pageContext.request.contextPath}/registration">
                        <c:if test="${inputHasErrors}">
                            <div class="alert alert-danger" role="alert">
                                <p><fmt:message key="registrationpage.form.wrong.input"/></p>
                            </div>
                        </c:if>

                        <c:if test="${inputLoginAlreadyTaken}">
                            <div class="alert alert-danger" role="alert">
                                <p><fmt:message key="registrationpage.form.login.is.taken"/></p>
                            </div>
                        </c:if>
                        <div class="form-group" >
                            <label for="username">notIntEmail</label>
                            <input type="text" class="form-control" id="username" name="username" placeholder="notInt" >
                        </div>
                        <div class="form-group">
                            <input type="password" class="form-control" id="password" name="password">
                        </div>
                        <div class="form-group">
                            <input type="password" class="form-control" id="passwordRepeat" name="passwordRepeat">
                        </div>
                        <button class="btn btn-success" type="submit"><fmt:message key="registrationpage.button.registration"/></button>
                        <a class="btn"  align="left" href="${pageContext.request.contextPath}/login"><fmt:message key="registrationpage.button.gotoLogin"/></a>
                    </form>
                </div>
            </div>
    </div>
    </div>
</div>
</body>
</html>