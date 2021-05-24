<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="go.model.State" %>
<%@ page import="go.model.Town" %>
<html>
<head>
    <title>Edit Town</title>
</head>
<body>

<h1>Edit Town</h1>

<form action="${pageContext.request.contextPath}/admin/towns/update/${town.id}" method="post">

    <label>Name</label>
    <input type="text" name="name" value="${town.name}" style="width:80%"/>

    <label>Count</label>
    <input type="text" name="count" value="${town.count}" />

    <label>Uri</label>
    <input type="text" name="townUri" value="${town.townUri}" />

    <label>State</label>
    <select name="stateId" style="display: block">
        <c:forEach items="${states}" var="state">
            <c:set var="selected" value=""/>
            <c:if test="${town.stateId == state.id}">
                <c:set var="selected" value="selected"/>
            </c:if>
            <option value="${state.id}" ${selected}>${state.name}</option>
        </c:forEach>
    </select>

    <br/>
    <input type="submit" class="button retro" value="Update" style="display:inline-block;margin:30px auto;"/>

</form>

</body>
</html>
