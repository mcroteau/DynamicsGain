<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty error}">
	<div class="notify">${error}</div>
</c:if>

<h1>Your Profile</h1>


<c:if test="${user.charity}">
	<h2 style="margin-top:30px;">Accepted Contributions</h2>
	<p>Below are your the contributions your organization has accepted thus far.</p>

	<c:if test="${acceptedSubscriptions.size() > 0}">
		<div id="subscription-details">
			<h3>Subscriptions</h3>

			<c:forEach var="subscription" items="${acceptedSubscriptions}">
				<p><strong>${subscription.amountZero}
				</strong> monthly
						${subscription.donationDate} by ${subscription.email}
					<c:if test="${subscription.cancelled}">
						<strong class="yellow">Cancelled</strong>
					</c:if>
				</p>
			</c:forEach>
		</div>
	</c:if>

	<c:if test="${acceptedCharges.size() > 0}">
		<h3 style="margin-top:30px;">One-Time Donations</h3>
		<c:forEach var="charge" items="${acceptedCharges}">
			<p><strong>${charge.amountZero}</strong> donated
					${charge.donationDate} by ${charge.email}
			</p>
		</c:forEach>
	</c:if>

	<c:if test="${acceptedCharges.size() == 0 && acceptedSubscriptions.size() == 0}">
		<p class="yellow">No current contributions.</p>
	</c:if>
</c:if>






<h2 style="margin-top:90px;">Your Contributions</h2>

<p>Below are your contribution details.</p>

<c:if test="${subscriptions.size() > 0}">
	<div id="subscription-details">
		<h3>Subscriptions</h3>

		<c:forEach var="subscription" items="${subscriptions}">
			<p><strong>${subscription.amountZero}
			</strong> monthly to
				<c:if test="${subscription.organization != null}">
					${subscription.organization.name}
				</c:if>
				<c:if test="${subscription.organization == null}">
					Go <strong>+Spirit</strong>
				</c:if>
					${subscription.donationDate}
				<c:if test="${!subscription.cancelled}">
					<a href="javascript:" class="button beauty small cancel" class="button beauty small"
					   data-subscription="${subscription.stripeId}">Cancel</a>
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
			<c:if test="${charge.organization != null}">
				${charge.organization.name}
			</c:if>
			<c:if test="${charge.organization == null}">
				Go <strong>+Spirit</strong>
			</c:if>
				${charge.donationDate}
		</p>
	</c:forEach>
</c:if>

<c:if test="${charges.size() == 0 && subscriptions.size() == 0}">
	<p class="yellow">No donations yet.</p>
	<p><a href="${pageContext.request.contextPath}/donate" class="href-dotted">Donate</a></p>
</c:if>


<c:if test="${charges.size() > 0 || subscriptions.size() > 0}">
	<p>Thank you for being a contributor.<br/>
		Contact us any time if you have questions.</p>
</c:if>



<c:if test="${user.charity && !user.activated}">
	<img src="${pageContext.request.contextPath}/assets/media/stripe.png" style="width:150px;"/>
	<p>If you have done this already, you don't need to do this again.
		You will be redirected to Stripe to complete the activation process.</p>
	<form action="${pageContext.request.contextPath}/admin/ownership/requests/activate/${user.id}" method="post">
		<input type="submit" value="Activate Account" class="button">
	</form>
</c:if>



<script>
	$(document).ready(function(){
		var $cancel = $('.cancel');

		$cancel.click(function(){
			var subscription = $(this).attr('data-subscription')
			remove(subscription)
		})

		var remove = function(subscription){
			return $.ajax({
				method: "post",
				url : "${pageContext.request.contextPath}/donation/cancel/" + subscription,
				success: success,
				error : error
			})
		}

		var success = function(){
			window.location.reload();
		}

		var error = function(ex){
			alert("Something went wrong, no worries, please contact us to resolve.")
			console.log(ex)
		}
	});
</script>
