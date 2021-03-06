<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty message}">
    <div class="notify">${message}</div>
</c:if>

<c:if test="${organization != null}">

    <div id="signup-form-container">

        <h1 class="yellow">${organization.name}</h1>

        <h4>Request Ownership of this Organization</h4>

        <p class="left" style="margin-bottom:10px;">Do you work for this organization? If not you may
            view organizations registered on this site and request ownership.</p>

        <p class="left">Once you own this organization you will be able to edit the
            details of the organization on this site and receive donations through
            this site and kiosks throughout local communities.</p>

        <h4>What happens after we claim ownership?</h4>

        <p class="left" style="margin-bottom:20px;">I will be calling you to
            verify your affiliation with the organization and
            will give you instructions on how to complete the registration
            process. Once you have completed the registration process you
            will be able to start receiving donations through our system.
            This means on our site, developer's sites and kiosks throughout
            local communities.</p>

        <c:if test="${!requested}">

            <h3>Ownership Form</h3>

            <p class="left" style="margin-bottom:30px;">Feel free to give us your contact details so we may
                begin verifying your affiliation with this organization.</p>

            <form action="${pageContext.request.contextPath}/ownership" method="post" id="registration-form">
                <fieldset style="text-align: left">

                    <input type="hidden" name="organizationId" value="${organization.id}"/>

                    <label>Contact Name</label>
                    <input type="email" placeholder="" name="name" style="width:80%;">

                    <label>Contact Email</label>
                    <input type="email" placeholder="" name="email" style="width:100%;">

                    <label>Contact Direct Number</label>
                    <input type="text" placeholder="" name="phone" style="width:60%;">

                </fieldset>
            </form>


            <div style="width:100%;margin-top:41px;text-align:center;margin-bottom:30px; ">
                <input type="submit" class="button retro" id="signup-button" value="Request Ownership" style="width:100%;"/>
            </div>

        </c:if>

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

</c:if>


<c:if test="${organization == null}">

    <h4>Request Ownership of this Organization</h4>

    <p class="left" style="margin-bottom:10px;">Do you work for this organization? If not you may
        view organizations registered on this site and request ownership.</p>

    <p class="left">Once you own this organization you will be able to edit the
        details of the organization on this site and receive donations through
        this site and kiosks throughout local communities.</p>

    <h4>What happens after we claim ownership?</h4>

    <p class="left" style="margin-bottom:20px;">I will be calling you to
        verify your affiliation with the organization and
        will give you instructions on how to complete the registration
        process. Once you have completed the registration process you
        will be able to start receiving donations through our system.
        This means on our site, developer's sites and kiosks throughout
        local communities.</p>

</c:if>