<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>${prospect.name} History</title>
</head>
<body>

<div class="inside-container">

    <c:if test="${not empty message}">
        <div class="notify">${message}</div>
    </c:if>
    <a href="${pageContext.request.contextPath}/prospects/${prospect.id}" id="prospect-back" class="href-dotted">&larr;&nbsp;Back</a>

    <h1>${prospect.name}</h1>
    <p>History</p>

    <c:if test="${activities.size() > 0}">
        <c:forEach items="${activities}" var="activity">
            <div style="border-bottom:solid 1px #ccc;margin-bottom:20px;padding-bottom:10px;" class="href-dotted-black">
                <div class="sales-activity"
                     style="font-size:23px;display:block;margin-bottom:10px;">
                    <span class="activity-date"><strong>${activity.prettyTime}</strong> : ${activity.name} &check;
                        <c:if test="${activity.effortId != null}">
                            Effort Id: ${activity.effortId}
                        </c:if>
                    </span>
                </div>
            </div>
        </c:forEach>
    </c:if>

    <c:if test="${activities.size() == 0}">
        <p>No completed activities yet.</p>
    </c:if>
</div>

</body>
</html>
