<%@ include file="../layout/metadata-standart.jsp" %>

<html lang="${param.lang}">
<head>
    <%@ include file="../layout/bootstrap.jsp" %>
    <title ><fmt:message key="userlist.page.header"/></title>
</head>
<body>
<%@ include file="../layout/header.jsp" %>
<%@ include file="../layout/userheader.jsp"%>
<div class="container">
    <div class="row">
        <div class="col-md-6">
            <div class="border border-info mt-2 rounded">
                <p><fmt:message key="userprofilepage.paragrapf.userMoneyInCents"/></p>
                <p>${sessionScope.user.userMoneyInCents}</p>
            </div>
        </div>
        <div class="col-md-6 ">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-heading"><fmt:message key="userprofilepage.form.header"/></h3>
                </div>
                <div class="panel-body">
                    <form class="form" method="post" action="${pageContext.request.contextPath}/user/userprofile">
                        <c:if test="${inputHasErrors}">
                            <div class="alert alert-danger" role="alert">
                                <p><fmt:message key="user-profile.form.wrong"/></p>
                            </div>
                        </c:if>
                        <div class="form-group">
                            <input type="number" class="form-control" id="money" name="money" >
                        </div>
                        <button class="btn btn-success" type="submit"><fmt:message key="userprofilepage.button.replenish"/></button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
