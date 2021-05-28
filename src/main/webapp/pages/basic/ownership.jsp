<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="parakeet" uri="/META-INF/tags/parakeet.tld"%>

<div id="signup-form-container">

    <c:if test="${not empty message}">
        <div class="notify">${message}</div>
    </c:if>

    <c:if test="${organization != null}">
        <h1 class="yellow">${organization.name}</h1>
    </c:if>
    <h4>Take Ownership of this Organization</h4>

    <p class="left" style="margin-bottom:10px;">Do you work for this organization? If not you may view organizations registered on this site and take ownership.</p>

    <h4>What happens after we claim ownership?</h4>

    <p class="left" style="margin-bottom:20px;">I will be calling you to verify your affiliation with the org and
        will give you instructions on how to complete the registration
        process. Once you have completed the registration process you
        will be able to start receiving donations through our system.
        This means on our site, developer's sites and kiosks throughout
        local communities.</p>

    <p class="left">Give us your contact details so we may verify your affiliation with this organization.</p>

    <form action="${pageContext.request.contextPath}/claim" method="post" id="registration-form">
        <fieldset style="text-align: left">

            <label>Contact Name</label>
            <input id="name" type="email" placeholder="Mike Croteau" name="name" style="width:80%;">

            <label>Contact Email</label>
            <input id="username" type="email" placeholder="mike@godynamics.cc" name="username" style="width:100%;">

            <label>Contact Direct Number</label>
            <input id="phone" type="text" placeholder="(907) 987-8652" name="phone" style="width:60%;">

        </fieldset>
    </form>


    <div style="width:100%;margin-top:41px;text-align:center;margin-bottom:30px; ">
        <input type="submit" class="button retro" id="signup-button" value="Take Ownership" style="width:100%;"/>
    </div>

    <br class="clear"/>

</div>


<script>

    var processing = false
    var form = document.getElementById("registration-form");
    var button = document.getElementById("signup-button");

    button.addEventListener("click", function(event){
        event.preventDefault();
        if(!processing){
            processing = true;
            form.submit();
        }
    })

</script>
