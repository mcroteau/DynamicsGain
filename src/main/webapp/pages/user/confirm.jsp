<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="reset-password-form">

	<c:if test="${not empty message}">
		<div class="span12">
			<div class="alert alert-info">${message}</div>
		</div>
	</c:if>

	<c:if test="${not empty error}">
		<div>
			<div class="alert alert-danger">${error}</div>
		</div>
	</c:if>


	<h1>Reset Password</h1>

	<form action="${pageContext.request.contextPath}/users/reset/${user.id}" modelAttribute="user" method="post" class="pure-form pure-form-stacked">

		<input type="hidden" name="id" value="${user.id}"/>

		<div class="form-group">
		  	<label for="password">${user.username}</label>
		</div>

        <br/>
		<div class="form-group">
		  	<label for="password" class="left">New Password</label>
		  	<input type="text" name="password" class="form-control" id="password" placeholder="Password" value="">
		</div>

        <br/>

		<div class="form-group">
			<input type="submit" class="button retro" id="reset-password" value="Reset Password"/>
		</div>

	</form>

</div>


