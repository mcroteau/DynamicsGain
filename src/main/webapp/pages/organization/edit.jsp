<%@ page import="go.model.Town" %>
<%@ page import="go.model.Organization" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Edit Organization</title>
</head>
<body>
<h1>Edit Organization</h1>
<form action="/z/admin/organizations/update" method="post">

    <input type="hidden" name="id" value="${organization.id}"/>
    <input type="hidden" name="count" value="${organization.count}"/>

    <label>Name</label>
    <input type="text" name="name" value="${organization.name}"/>

    <label>Uri</label>
    <input type="text" name="organizationUri" value="${organization.organizationUri}" />

    <label>Town</label>
    <select name="townId" style="display: block">
        <c:forEach items="${towns}" var="town">
            <%
                String selected = "";
                Organization organization = (Organization) request.getAttribute("organization");
            %>
            <c:if test="${organization.townId == town.id}">
                <%selected = "selected";%>
            </c:if>

            <option value="${town.id}" <%=selected%>>${town.name}</option>
        </c:forEach>
    </select>

    <label>Description</label>
    <textarea name="description">${organization.description}</textarea>


    <input type="submit" class="button retro" value="Update" style="display:inline-block;margin:30px auto;"/>

</form>
</body>
</html>
