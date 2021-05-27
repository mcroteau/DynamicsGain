<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Cities/Towns</title>
</head>
<body>

    <p class="yellow" style="display: inline-block">Please help! <strong class="highlight">${count} in need!</strong></p>

    <h1 id="towns">Towns/Cities</h1>
    <ul>
        <c:forEach var="town" items="${towns}">
            <li style="padding:4px 0px;"><a href="${pageContext.request.contextPath}/towns/${town.townUri}" class="href-dotted" style="font-size:27px;">${town.name}
                <span style="display:block"><strong class="" style="line-height:1.4em;">${town.count}</strong> homeless</span></a></li>
        </c:forEach>
    </ul>

</body>
</html>
