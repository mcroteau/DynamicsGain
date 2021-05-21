<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Create Town</title>
</head>
<body>
<h1>Create Town</h1>
<form action="/z/admin/towns/save" method="post">

    <label>Name</label>
    <input type="text" name="name" />

    <label>Uri</label>
    <input type="text" name="townUri" />

    <input type="submit" class="button retro" value="Save" style="display:inline-block;margin:30px auto;"/>
</form>
</body>
</html>
