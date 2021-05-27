<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Organizations</title>
</head>
<body>

    <style>
        h3{
            margin:40px auto 4px !important;
        }
        table{
            margin:0px auto 40px auto;
        }
    </style>

    <p class="yellow" style="display: inline-block">Please help! <strong class="highlight">${count} in need!</strong></p>

    <h1>Organizations</h1>

    <p>Below is a list of all Organizations registered in our system.</p>

    <c:forEach var="town" items="${towns}">
        <h3>${town.name}</h3>
        <table>
            <c:forEach var="organization" items="${town.organizations}">
                <tr>
                    <td class="center" style="padding-left:10px;width:50%">
                        <a href="${pageContext.request.contextPath}/donate/${organization.id}" class="href-dotted">${organization.name}</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:forEach>

    <p>or</p>

    <p>Give to Go <strong>+Spirit</strong></p>

    <div style="margin-bottom:70px;">
        <a href="${pageContext.request.contextPath}/donate" class="button light small">Give Now +</a>
    </div>

</body>
</html>
