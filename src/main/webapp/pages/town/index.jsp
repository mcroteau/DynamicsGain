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
        <strong class="yellow" style="line-height: 1.3em;font-size:131px;">${town.countZero}</strong><br/> <span>Homeless <span class="header-information"> in
            <strong class="highlight" style="font-size:89px;">${town.name}</strong></span></span>
    </p>

    <p class="open-text left">Go +Dynamics is a non profit designed with
        the sole purpose of removing barriers that prevent people from
        giving time, money and resources to those in need!</p>

    <p class="left">Select an organization to get more information or to donate to them.</p>


    <h3>Organizations &amp; Shelters</h3>
    <table style="width:100%; text-align: center">
        <c:forEach var="organizations" items="${organizations}">
            <tr>
                <td class="center">
                    <a href="${pageContext.request.contextPath}/donate/${organizations.id}" class="href-dotted">${organizations.name}</a>
                </td>
            </tr>
        </c:forEach>
    </table>

    <p>or</p>

    <p>Give to Go <strong>+Dynamics</strong></p>

    <div style="margin-bottom:70px;">
        <a href="${pageContext.request.contextPath}/donate" class="button beauty small">Give +</a>
    </div>

</body>
</html>
