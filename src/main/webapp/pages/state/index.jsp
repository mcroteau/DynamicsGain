<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>${town.name} Organizations</title>
</head>
<body>

    <c:if test="${not empty message}">
        <div class="notify">${message}</div>
    </c:if>

    <p style="font-size:17px;">Please help!</p>
    <br/>
    <p id="welcome-text">
        <strong class="yellow" style="line-height: 1.3em;">${count}</strong><br/> <span>Homeless <span class="header-information"> in
            <strong class="highlight">${state.name}</strong></span></span>
    </p>

    <p class="open-text left">Go +Spirit is a non profit designed with
        the sole purpose of removing barriers that prevent people from
        giving time, money and resources to those in need!</p>

    <p class="left">Select an Town/City to get more information or to donate to them.</p>

    <h3>Towns &amp; Cities</h3>
    <table>
        <c:forEach var="town" items="${towns}">
            <tr>
                <td class="center">
                    <a href="${pageContext.request.contextPath}/towns/${town.townUri}" class="href-dotted">${town.name} ${town.countZero}</a>
                </td>
            </tr>
        </c:forEach>
    </table>

    <p>or</p>

    <p>Give to Go <strong>+Spirit</strong></p>

    <div style="margin-bottom:70px;">
        <a href="/z/donate" class="button beauty small">Give +</a>
    </div>

</body>
</html>