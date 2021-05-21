<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Organizations</title>
</head>
<body>
<h1>Organizations</h1>
<a href="/z/admin/organizations/create" class="href-dotted">+ New Organization</a>
<c:if test="${organizations.size() > 0}">
    <table>
        <tr>
            <th></th>
            <th>Town ID</th>
            <th>Count</th>
            <th></th>
        </tr>
        <c:forEach var="organization" items="${organizations}">
            <tr>
                <td><a href="/z/admin/organizations/edit/${organization.id}" class="href-dotted-black">${organization.name}</a></td>
                <td class="center">${organization.townId}</td>
                <td class="center"><a href="/z/admin/count/${organization.id}" class="href-dotted-black">${organization.count}</a></td>
                <td class="right">
                    <form action="/z/admin/organizations/delete/${organization.id}" method="post">
                        <input type="submit" class="button small beauty" value="Delete" onclick="return confirm('Are you sure you want to delete Organization?');"/>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</c:if>

<c:if test="${organizations.size() == 0}">
    <p>No towns created yet.</p>
</c:if>
</body>
</html>
