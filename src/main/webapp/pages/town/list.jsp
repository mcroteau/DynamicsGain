<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Towns</title>
</head>
<body>
<h1>Towns</h1>
<a href="/z/admin/towns/create" class="href-dotted">+ New Town</a>
<c:if test="${towns.size() > 0}">
    <table>
        <c:forEach var="town" items="${towns}">
            <tr>
                <td>${town.id}</td>
                <td><a href="/z/admin/towns/edit/${town.id}" class="href-dotted-black">${town.name}</a></td>
                <td>${town.townUri}</td>
                <td class="right">
                    <form action="/z/admin/towns/delete/${town.id}" method="post">
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
