    <body th:fragment="userHeader">
        <div class="row border border-secondary">
        <nav class="navbar navbar-default col-md-12",>
        <div class="container-fluid row">
        <div class="col-md-10 row justify-content-start">

        <a class="col-2" th:href="@{/user/delivers-to-get}" th:text="#{layout.userheader.not.gotten.delivers}"></a>
        <a class="col-1" th:href="@{/user/userprofile}" th:text="#{layout.userheader.balance}"></a>
        <a class="col-2" th:href="@{/user/user-delivery-initiation}" th:text="#{layout.userheader.delivery.initiation}"></a>
        <a class="col-2" th:href="@{/user/user-delivery-request-confirm}" th:text="#{layout.userheader.delivery.pay}"></a>
        <a class="col-2" th:href="@{/user/user-statistic}" th:text="#{layout.userheader.user.statistic}"></a>
        </div>
        </div>
        </nav>
        </div>
        </body>