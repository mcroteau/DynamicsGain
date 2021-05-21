<html>
<head>
    <title>Edit Town</title>
</head>
<body>

<h1>Edit Town</h1>

<form action="/z/admin/towns/update" method="post">

    <input type="hidden" name="id" value="${town.id}"/>

    <label>Name</label>
    <input type="text" name="name" value="${town.name}"/>

    <label>Count</label>
    <input type="text" name="count" value="${town.count}" />

    <label>Uri</label>
    <input type="text" name="townUri" value="${town.townUri}" />


    <input type="submit" class="button retro" value="Update" style="display:inline-block;margin:30px auto;"/>

</form>

</body>
</html>
