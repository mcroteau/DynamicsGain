<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<title>Edit Profile</title>
</head>
<body>

	<div class="inside-container">

		<c:if test="${not empty message}">
			<div class="notify">${message}</div>
		</c:if>

		<h1>Your Profile</h1>

		<c:if test="${activityCounts.size() > 0}">
			<h3>Great Job!</h3>
			<p>You're always doing a great job.
				Let's see what you've been up to.</p>
			<c:forEach items="${activityCounts}" var="activityCount">
				<p>${activityCount.count} ${activityCount.name}s</p>
			</c:forEach>
			<p>Not bad...</p>
		</c:if>
		<c:if test="${activityCounts.size() == 0}">
			Nothing to show yet.
		</c:if>

		<form action="${pageContext.request.contextPath}/users/update/${user.id}" method="post">

			<label>Phone</label>
			<p class="information">Used to send Activity Reminders via Text.</p>
			<input type="text" name="phone" placeholder="+19076879557" value="${user.phone}"/>

			<label>Email</label>
			<input type="text" name="username" placeholder="mail@mail.org" value="${user.username}" style="width:80%;"/>

			<input type="submit" value="Update Profile" class="button" style="display:block;margin:40px 0px 140px 0px;"/>
		</form>


		<div style="text-align: right">
			<a href="${pageContext.request.contextPath}/signout" class="href-dotted">Signout</a>
		</div>
	</div>
</body>
</html>

