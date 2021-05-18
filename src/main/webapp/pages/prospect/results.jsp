<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Search Results</title>
</head>
<body>

<%--<div class="go-styles">--%>
<%--    <a href="#" class="une color"></a>--%>
<%--    <a href="#" class="deux color"></a>--%>
<%--    <a href="#" class="trois color"></a>--%>
<%--    <a href="#" class="quatre color"></a>--%>
<%--    <br class="clear"/>--%>
<%--</div>--%>

<div class="inside-container">

    <h1 id="results-h1">Results</h1>
    <a href="${pageContext.request.contextPath}/prospects/create" class="href-dotted" id="new-prospect-href">+ New Prospect</a>
    <br class="clear"/>

    <style>
        #new-prospect-href{
            float: right;
        }
        #results-h1{
            float:left;
        }
        #results{
            width:100%;
            font-size:24px;
        }
        #results a{
            font-size:29px;
        }
    </style>

    <c:if test="${prospects.size() > 0}">
        <table id="results">
            <tr>
                <th></th>
                <th></th>
            </tr>
            <c:forEach var="prospect" items="${prospects}">
                <tr>
                    <td><a href="${pageContext.request.contextPath}/prospects/${prospect.id}" class="href-dotted-black">${prospect.name}</a></td>
                    <td class="center" style="width:190px;">${prospect.phone}</td>
                </tr>
            </c:forEach>
        </table>
    </c:if>

    <c:if test="${prospects.size() == 0}">
        <p>No Prospects created yet.</p>
    </c:if>
</div>
</body>
</html>
