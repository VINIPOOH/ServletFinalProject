    <body th:fragment="userHeader">
        <div class="row border border-secondary">
        <nav class="navbar navbar-default col-md-12",>
        <div class="container-fluid row">
        <div class="col-md-10 row justify-content-start">


        <a class="col-2" href="${pageContext.request.contextPath}/user/delivers-to-get" ><fmt:message key="layout.userheader.not.gotten.delivers"/></a>
        <a class="col-1" href="${pageContext.request.contextPath}/user/userprofile" ><fmt:message key="layout.userheader.balance"/></a>
        <a class="col-2" href="${pageContext.request.contextPath}/user/user-delivery-initiation" ><fmt:message key="layout.userheader.delivery.initiation"/></a>
        <a class="col-2" href="${pageContext.request.contextPath}/user/user-delivery-request-confirm" ><fmt:message key="layout.userheader.delivery.pay"/></a>
            <a class="col-2" href="${pageContext.request.contextPath}/user/user-statistic?page=1&size=10" ><fmt:message
            key="layout.userheader.user.statistic"/></a>
        </div>
        </div>
        </nav>
        </div>
        </body>