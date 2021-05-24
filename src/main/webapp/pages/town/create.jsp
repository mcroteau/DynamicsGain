<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Create Town</title>
</head>
<body>
<h1>Create Town</h1>
<form action="${pageContext.request.contextPath}/admin/towns/save" method="post">

    <label>Name</label>
    <input type="text" name="name" style="width:80%"/>

    <label>Uri</label>
    <input type="text" name="townUri" />

    <label>State</label>
    <select name="stateId" style="display: block">
        <c:forEach items="${states}" var="state">
            <option value="${state.id}">${state.name}</option>
        </c:forEach>
    </select>


    <input type="submit" class="button retro" value="Save" style="display:inline-block;margin:30px auto;"/>
</form>
</body>
</html>
