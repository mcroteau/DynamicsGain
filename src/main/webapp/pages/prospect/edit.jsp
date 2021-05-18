<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="go.model.Prospect" %>
<html>
<head>
    <title>Edit Prospect</title>
</head>
<body>

    <div class="inside-container">

        <a href="${pageContext.request.contextPath}/prospects/${prospect.id}" id="prospect-back" class="href-dotted">&larr;&nbsp;Back</a>

        <c:if test="${not empty message}">
            <div class="notify">${message}</div>
        </c:if>

        <h1>Edit Prospect</h1>

        <form action="${pageContext.request.contextPath}/prospects/update" method="post">

            <input type="hidden" name="id" value="${prospect.id}"/>

            <label>Name</label>
            <input type="text" name="name" value="${prospect.name}" style="width:90%;"/>

            <label>Phone</label>
            <input type="text" name="phone" value="${prospect.phone}" style="width:70%"/>

            <label>Status</label>
            <select name="statusId" style="display: block">
                <c:forEach items="${statuses}" var="status">
                    <%String selected = "";%>
                    <c:if test="${prospect.statusId == status.id}">
                        <%selected = "selected";%>
                    </c:if>

                    <option value="${status.id}" <%=selected%>>${status.name}</option>
                </c:forEach>
            </select>

            <label>Contacts</label>
            <textarea name="contacts" placeholder="Robin Tout (810) 291-1235">${prospect.contacts}</textarea>


            <input type="submit" class="button" value="Update Prospect" style="display:inline-block;margin:30px auto;"/>

        </form>
    </div>

</body>
</html>
