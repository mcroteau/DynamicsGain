<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>States</title>
</head>
<body>
<style>
    li{
        list-style: decimal;
    }
</style>
<h1 style="float:left">States</h1>
<a href="${pageContext.request.contextPath}/admin/towns/create" class="href-dotted" style="margin:20px 0px 0px 10px;display: inline-block">+ New Town</a>
<br class="clear"/>

<c:if test="${states.size() > 0}">
    <table>
        <c:forEach var="state" items="${states}">
            <tr>
                <td>${state.id}</td>
                <td><strong>${state.name}</strong></td>
                <td>
                    <c:if test="${state.towns.size() > 0}">
                        <ul>
                            <c:forEach items="${state.towns}" var="town">
                                <li>${town.name}</li>
                            </c:forEach>
                        </ul>
                    </c:if>
                    <c:if test="${state.towns.size() == 0}">
                        <p>No towns. <a href="${pageContext.request.contextPath}/admin/towns/create" class="href-dotted">+ New Town</a></p>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>
</c:if>

<c:if test="${states.size() == 0}">
    <p>No states created yet.</p>
</c:if>
</body>
</html>
