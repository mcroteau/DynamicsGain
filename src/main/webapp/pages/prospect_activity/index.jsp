<%@ page import="java.util.TimeZone" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Add Activity</title>
</head>
<body>

<%--<div class="go-sig-left">--%>
<%--    <span class="uno color"></span>--%>
<%--    <span class="dos color"></span>--%>
<%--    <span class="tres color"></span>--%>
<%--    <span class="quatro color"></span>--%>
<%--    <span class="cinco color"></span>--%>
<%--    <span class="seies color"></span>--%>
<%--    <span class="siete color"></span>--%>
<%--    <br class="clear"/>--%>
<%--</div>--%>

<div class="inside-container">

    <a href="${pageContext.request.contextPath}/prospects/${prospect.id}" id="prospect-back" class="href-dotted">&larr;&nbsp;Back</a>


    <h1>${prospect.name}</h1>
    <h3>Add Sales Activity</h3>
    <form action="${pageContext.request.contextPath}/prospects/activity/save/${prospect.id}" method="post">

        <label>Sales Activity</label>
        <select name="activity-id" style="display: block">
            <c:forEach items="${activities}" var="activity">
                <option value="${activity.id}">${activity.name}</option>
            </c:forEach>
        </select>

        <label>Sales Activity Date</label>
        <input type="date" id="date" name="activity-date"/>

        <label>Sales Activity Time : Expects 24 hour format.</label>
        <input type="number" name="activity-hour" maxlength="2" placeholder="23" style="width:105px;display:inline-block" oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"/>
        <input type="number" name="activity-minute" maxlength="2" placeholder="01" style="width:105px;" oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"/>

        <p class="information">Timezone: <span id="timezone-p"></span></p>

        <input type="hidden" name="timezone" id="timezone"/>

        <br/>
        <input type="submit" value="Add Activity" class="button" style="margin-top:20px;"/>
    </form>
</div>

<script>
    var date = new Date();
    var d = date.toDateString();
    document.querySelector('#date').valueAsDate = new Date();
    const tzid = Intl.DateTimeFormat().resolvedOptions().timeZone;
    document.querySelector('#timezone').value = tzid;
    document.querySelector('#timezone-p').innerHTML = tzid;
</script>
</body>
</html>
