<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Towns</title>
</head>
<body>
<h1 style="float:left">Towns</h1>
<a href="${pageContext.request.contextPath}/admin/towns/create" class="href-dotted"  style="float:left;margin:24px 0px 0px 30px;">+ New Town</a>
<br/>

<c:if test="${towns.size() > 0}">
    <table>
        <c:forEach var="town" items="${towns}">
            <tr>
                <td>${town.id}</td>
                <td><a href="${pageContext.request.contextPath}/admin/towns/edit/${town.id}" class="href-dotted-black">${town.name}</a></td>
                <td>${town.townUri}</td>
                <td class="right">
                    <form action="${pageContext.request.contextPath}/admin/towns/delete/${town.id}" method="post">
                        <input type="submit" class="button small beauty" value="Delete" onclick="return confirm('Are you sure you want to delete Town?');"/>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</c:if>

<c:if test="${towns.size() == 0}">
    <p>No towns created yet.</p>
</c:if>
</body>
</html>
