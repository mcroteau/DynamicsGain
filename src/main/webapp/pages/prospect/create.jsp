<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>New Prospect</title>
</head>
<body>

<div class="inside-container">

    <h1>New Prospect</h1>
    <form action="${pageContext.request.contextPath}/prospects/save" method="post">

        <label>Name</label>
        <input type="text" name="name" style="width:100%;" />

        <label>Phone</label>
        <input type="text" name="phone" />

        <label>Status</label>
        <select name="status" style="display: block">
            <c:forEach items="${statuses}" var="status">
                <option value="${status.id}">${status.name}</option>
            </c:forEach>
        </select>

        <input type="submit" class="button retro" value="Save Prospect" style="display:inline-block;margin:30px auto;"/>
    </form>

    <a href="${pageContext.request.contextPath}/prospects" style="margin-top:50px;display:inline-block;" class="href-dotted">&larr;&nbsp;Back</a>


</div>
</body>
</html>
