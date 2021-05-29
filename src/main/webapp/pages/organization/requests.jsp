<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Ownership Requests</title>
</head>
<body>
<h1>Ownership Requests</h1>

<c:if test="${not empty message}">
    <div class="notify">${message}</div>
</c:if>

<c:if test="${reqs.size() > 0}">
    <table>
        <tr>
            <th></th>
            <th></th>
            <th></th>
        </tr>
        <c:forEach var="req" items="${reqs}">
            <tr>
                <td class="left">${req.name}</td>
                <td class="center">${req.email}</td>
                <td class="center">${req.phone}</td>
                <td class="right">
                    <c:if test="${req.approved}">
                        &check;
                    </c:if>
                    <c:if test="${!req.approved}">
                        <form action="${pageContext.request.contextPath}/admin/ownership/requests/approve/${req.id}" method="post">
                            <input type="submit" class="button small" value="Approve" onclick="return confirm('Are you sure you want to Approve?');"/>
                        </form>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>
</c:if>

<c:if test="${reqs.size() == 0}">
    <p>No requests submitted yet.</p>
</c:if>
</body>
</html>
