<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Organizations</title>
</head>
<body>
<h1 style="float:left">Organizations</h1>
<a href="${pageContext.request.contextPath}/admin/organizations/create" class="href-dotted" style="float:left;margin:24px 0px 0px 30px;">+ New Organization</a>

<c:if test="${not empty message}">
    <div class="notify">${message}</div>
</c:if>

<c:if test="${organizations.size() > 0}">
    <table>
        <tr>
            <th></th>
            <th>Town ID</th>
            <th></th>
        </tr>
        <c:forEach var="organization" items="${organizations}">
            <tr>
                <td><a href="${pageContext.request.contextPath}/admin/organizations/edit/${organization.id}" class="href-dotted-black">${organization.name}</a></td>
                <td class="center">${organization.townId}</td>
                <td class="right">
                    <form action="${pageContext.request.contextPath}/admin/organizations/delete/${organization.id}" method="post">
                        <input type="submit" class="button small beauty" value="Delete" onclick="return confirm('Are you sure you want to delete Organization?');"/>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</c:if>

<c:if test="${organizations.size() == 0}">
    <p>No organizations created yet.</p>
</c:if>
</body>
</html>
