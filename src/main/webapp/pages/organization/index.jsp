<html>
<head>
    <title>${organization.name}</title>
</head>
<body>

    <p>Please help!</p>

    <h1>${organization.name}</h1>
    <p>${organization.description}</p>

    <h3>Give to ${organization.name}</h3>
    <p>You can make a one-time or a reoccurring donation that goes
        directly to ${organization.name}.</p>
    <a href="${pageContext.request.contextPath}/donate/${organization.id}" class="button super beauty">Give Now +</a>

</body>
</html>
