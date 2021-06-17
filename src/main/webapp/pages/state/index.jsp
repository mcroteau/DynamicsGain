<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>${state.name} Towns</title>

</head>
<body>

<div class="center">

    <c:if test="${not empty message}">
        <div class="notify">${message}</div>
    </c:if>

    <p class="yellow highlight" style="font-size:17px;">Please help!</p>
    <br/>


    <div id="welcome-text">
        <strong class="yellow" style="line-height: 1.3em;font-size:131px;">${count}</strong><br/>
        <span>Homeless <span class="header-information"> in
            <strong class="highlight" style="font-size:42px;">${state.name}</strong></span></span>
    </div>

    <p class="open-text left">Dynamics +Gain is a non profit designed with
        the sole purpose of removing barriers that prevent people from
        giving time, money and resources to those in need!</p>

    <p class="left">Select an Town/City to get more information or to donate to them.</p>

    <h3>Towns &amp; Cities</h3>
    <table style="width:100%;text-align: center">
        <c:forEach var="town" items="${towns}">
            <tr>
                <td class="center">
                    <a href="${pageContext.request.contextPath}/towns/${town.townUri}" class="href-dotted">${town.name} ${town.countZero}</a>
                </td>
            </tr>
        </c:forEach>
    </table>

    <p>or</p>

    <p>Give to Dynamics <strong>+Gain</strong></p>

    <div style="margin:10px auto 70px auto;">
        <a href="${pageContext.request.contextPath}/donate" class="button beauty small center">Give +</a>
    </div>

</div>

</body>
</html>
