<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Signin</title>
</head>
<body>

<div style="margin-top:20px;">

    <c:if test="${not empty message}">
        <p class="notify">${message}</p>
    </c:if>

    <h2>Signin</h2>

    <form action="${pageContext.request.contextPath}/authenticate" method="post" >

        <div class="form-group">
            <label for="username" class="left">Username</label>
            <input type="text" name="username" class="form-control" id="username" placeholder=""  value=""  style="width:100%;">
        </div>

        <div class="form-group">
            <label for="password" class="left">Password</label>
            <input type="password" name="password" class="form-control" id="password" style="width:100%;" value=""  placeholder="&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;">
        </div>

        <div style="text-align:right; margin:30px 0px;">
            <input type="submit" class="button retro" value="Signin" style="width:100%;">
        </div>

    </form>

    <div id="signup-container" style="text-align: center;margin:21px auto 30px auto">
<%--        <a href="${pageContext.request.contextPath}/signup" class="href-dotted">Sign Up!</a>&nbsp;&nbsp;--%>
        <a href="${pageContext.request.contextPath}/users/reset" class="href-dotted">Forgot Password</a>&nbsp;&nbsp;
    </div>

</div>

</body>
</html>
