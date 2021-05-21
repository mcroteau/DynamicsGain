<html>
<head>
    <title>Go +Spirit: ${organization.name}</title>
</head>
<body>

    <p>Please help!</p>

    <h1>${organization.name}</h1>
    <h2><strong class="highlight">${organization.count}</strong>&nbsp; in need.</h2>
    <p>${organization.description}</p>

    <h3>Give to ${organization.name}</h3>
    <p>You can make a one-time or a reoccurring donation that goes
        directly to ${organization.name}.</p>
    <a href="/z/donate/organizations/${organization.id}" class="button super yellow">Give Now +</a>

</body>
</html>
