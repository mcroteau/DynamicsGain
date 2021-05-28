<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty error}">
	<div class="notify">${error}</div>
</c:if>

<h1>Your Profile</h1>
<p>Below are your contribution details.</p>

<c:if test="${subscriptions.size() > 0}">
	<div id="subscription-details">
		<h3>Subscriptions</h3>

		<c:forEach var="subscription" items="${subscriptions}">
			<p><strong>${subscription.amountZero}
			</strong> monthly to
				<c:if test="${subscription.location != null}">
					${subscription.location.name}
				</c:if>
				<c:if test="${subscription.location == null}">
					Dynamics <strong>+Gain</strong>
				</c:if>
					${subscription.donationDate}
				<c:if test="${!subscription.cancelled}">
					<a href="javascript:" class="button beauty small cancel" class="button beauty small"
					   data-subscription="${subscription.stripeId}"
							<c:if test="${subscription.location != null}">
								data-location="${subscription.location.id}"
							</c:if>
					>Cancel</a>
				</c:if>
				<c:if test="${subscription.cancelled}">
					<strong class="yellow">Cancelled</strong>
				</c:if>
			</p>
		</c:forEach>
	</div>
</c:if>

<c:if test="${charges.size() > 0}">
	<h3 style="margin-top:30px;">One-Time Donations</h3>
	<c:forEach var="charge" items="${charges}">
		<p><strong>${charge.amountZero}</strong> donated to
			<c:if test="${charge.location != null}">
				${charge.location.name}
			</c:if>
			<c:if test="${charge.location == null}">
				Go <strong>+Dynamics</strong>
			</c:if>
				${charge.donationDate}
		</p>
	</c:forEach>
</c:if>

<c:if test="${charges.size() == 0 && subscriptions.size() == 0}">
	<p>No current donations.</p>
	<p><a href="/z/donate" class="href-dotted">Donate</a></p>
</c:if>


<c:if test="${charges.size() > 0 || subscriptions.size() > 0}">
	<p>Thank you for being a contributor.<br/>
		Contact us any time if you have questions.</p>
</c:if>


<a href="${pageContext.request.contextPath}/user/edit_password/${user.id}" class="href-dotted" style="display:inline-block;margin-top:60px;">Update Password</a>



<script>
	$(document).ready(function(){
		var $cancel = $('.cancel');

		$cancel.click(function(){
			var subscription = $(this).attr('data-subscription')
			var location = $(this).attr('data-location')
			console.log(location)
			if(location != null){
				console.log('location exists')
				removeByLocation(location, subscription)
			}
			if(location == null){
				remove(subscription)
			}
		})

		var remove = function(subscription){
			return $.ajax({
				method: "Delete",
				url : "${pageContext.request.contextPath}/donation/cancel/" + subscription,
				success: success,
				error : error
			})
		}

		var removeByLocation = function(location, subscription){
			return $.ajax({
				method: "Delete",
				url : "${pageContext.request.contextPath}/donation/cancel/" + location + "/" + subscription,
				success: success,
				error : error
			})
		}

		var success = function(){
			window.location.reload();
		}

		var error = function(ex){
			alert(ex.toString());
		}
	});
</script>
