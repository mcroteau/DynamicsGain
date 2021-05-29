<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="parakeet" uri="/META-INF/tags/parakeet.tld" %>
<html>
<head>
    <title>Go +Spirit: Make Donation</title>
</head>
<body>
    <style>
        h1{
            margin:0px 0px 10px ;
            line-height: 1.0em;
        }
        #modal{
            position: fixed;
            top:0px;
            bottom:0px;
            left:0px;
            right:0px;
            margin:0px;
            background:#fff;
            display:none;
        }
        .message{
            margin-top:52px;
        }
        .duration{
            box-shadow: none !important;
        }
        .option{
            display: inline-block;
            vertical-align: middle;
            box-shadow: none !important;
        }
        #make-donation-container{
            text-align: left;
        }
        #custom,
        #custom:hover{
            color:#fff !important;
            background: #7D5ABF;
            border:solid 3px #7D5ABF;
            padding: 12px 12px 11px 12px !important;
            display: inline-block;
            font-weight: 900;
            box-shadow: none !important;
            text-align: center;
            text-transform: none;
        }
        #custom,
        #custom:hover{
            width:100px;
            color: #c4d3dd !important;
            background: #F3F3F7;
            border:solid 3px #F3F3F7;
        }
        #custom::placeholder{
            color: #8c96a9 !important;
        }
        #custom.active{
            color:#000 !important;
            background: #fff;
            border:dashed 3px #2234A3;
        }
        .button.active{
            color:#fff;
            background: #00a1ff;
            border:solid 3px #8fd6ff;
            border:solid 3px #eeef07;
            background: #2234A3;
            border:dashed 3px #fdfe01;
            border:dashed 3px #2234A3;
            background: #fdfe01;
            background: #f540a6;
            border:solid 3px #ff7bc5;
            font-weight: 900;
        }
        .light.active:hover{
            background:#3fb8ff;
        }
        .light:hover{
            background: #efefef;
        }
        .cc-details{
            float:left;
            margin-right:20px;
        }
        .cc-details input[type="text"]{
            text-align: center;
        }
        #exp-month{
            width:90px;
        }
        #exp-year{
            width:120px;
        }
        #cvc{
            width:110px;
        }
    </style>


    <c:if test="${organization != null}">
        <h1><c:if test="${inDonationMode}">Give to</c:if>
                ${organization.name}</h1>

        <c:if test="${inDonateMode}">
            <p class="information" style="font-size: 19px">Organization Id: <strong class="yellow">${organization.id}</strong></p>
        </c:if>

        <p style="white-space: pre-line; " class="left"><c:out value="${organization.description}" escapeXml="false" /></p>

        <c:if test="${inDonateMode}">
            <p>You can make a one-time or a reoccurring donation that goes
                directly to ${organization.name}.</p>
            <p class="live">Make Donation<strong class="">+</strong></p>
        </c:if>
    </c:if>

    <c:if test="${organization == null}">
        <h1>Give to Go <strong class="fun">+Spirit</strong></h1>
        <p class="left">All donations will go towards abolishing/destroying homelessness
        any which way we can. Kiosks, local Community Housing organizations
            and anything else that may move us forward.</p>
        <p class="left">You can make a one-time or a reoccurring donation that goes
            to Go <strong>+Spirit</strong>.</p>
    </c:if>

    <c:if test="${!inDonateMode}">
        <p class="yellow" style="display:inline-block">Contact to make a donation</p>
        <p>or</p>
        <p>Let them know you believe in this idea and encourage them to use
        claim their space on our site.</p>

        <p class="left" style="margin-top:20px;">Are you someone who helps make decisions for <strong>${organization.name}</strong>?
            You can register with us, claim this organization and be part of the
            movement. After registering and entering banking details, you will be
            able to receive donations on our site or at kiosks hopefully within
            your community.
            <a href="${pageContext.request.contextPath}/ownership?id=${organization.id}" class="href-dotted">Are you the owner of ${organization.name}?</a>
            And would like to be part of a movement?
        </p>

        <div style="margin:20px auto 30px;">
            <p>or</p>
            <p>Give to Go +Spirit</p>
            <a href="${pageContext.request.contextPath}/donate" class="button beauty">Give +</a>
        </div>
    </c:if>


    <input type="hidden" name="organizationId" value="${organization.id}" id="organization-id"/>


    <c:if test="${inDonateMode}">

        <p class="open-text live">Please select from the following:</p>

        <div id="donation-options" class="live">
            <div id="donation-durations">
                <button class="button retro small active duration">Give Once</button>
                <button class="button small sky duration" data-recurring="true">Give Monthly</button>
            </div>

            <a href="javascript:" class="option button sky active" id="fiver" data-amount="5">$5</a>&nbsp;
            <a href="javascript:" class="option button sky" data-amount="15">$15</a>&nbsp;
            <a href="javascript:" class="option button sky" data-amount="30">$30</a>&nbsp;
    <%--        <a href="javascript:" class="option button sky" data-amount="40">$40</a>&nbsp;--%>
    <%--        <br/><br/>--%>
            <input type="text" class="option button" id="custom" placeholder="Other Amount" style="width:150px;" data-amount="0"/>
        </div>

        <input type="hidden" name="amount" id="amount-input" value=""/>

        <div id="make-donation-container" class="live" style="display:none;">

            <label>credit card information</label>
            <input type="number" id="credit-card" name="credit-card" placeholder="4242424242424242" maxlength="16"/>

            <div class="cc-details">
                <label>month</label>
                <input type="number" id="exp-month"name="exp-month" placeholder="09" maxlength="2"/>
            </div>

            <div class="cc-details">
                <label>year</label>
                <input type="number" id="exp-year" name="exp-year" placeholder="2027" maxlength="4"/>
            </div>

            <div class="cc-details">
                <label>cvc</label>
                <input type="number" id="cvc" name="cvc" placeholder="123" maxlength="3"/>
            </div>

            <br class="clear"/>

            <label>Email
                <parakeet:isAuthenticated><span class="highlight">Signed in</span></parakeet:isAuthenticated>
            </label>
            <input type="text" id="email" value="<parakeet:username/>" placeholder="mail@dynamicsgain.org"/>


            <div style="text-align: center;">
                <a href="javascript:" id="donate-button" class="button super beauty amount" style="box-shadow:none !important;text-transform:none;border:solid 3px #ff7bc5">Donate</a>
                <p id="contribution-type" class="information">One Time Donation</p>
            </div>

        </div>


        <div id="modal">
            <div id="processing" class="message" style="display:block">
                <h3>Processing... please wait</h3>
                <img src="${pageContext.request.contextPath}/assets/media/processing.gif" style="margin:20px auto;"/>
                <p>Your donation is being processed. <br/>Thank you for your patience.</p>
            </div>

            <div id="success" class="message" style="display:none">
                <h1><strong id="donation-amount"></strong> Donated</h1>
                <h2 id="organization"></h2>
                <h3>Thank you!</h3>
                <p>Successfully processed your donation.</p>

                <p>Your username : <strong id="username"></strong></p>
                <p>Your temporary password : <strong id="password"></strong></p>

                <p><a href="${pageContext.request.contextPath}/signin" class="href-dotted">Signin</a></p>

                <a href="${pageContext.request.contextPath}/home" class="href-dotted">Take me home...</a>
            </div>

            <div id="error-container" class="message" style="display:none">
                <h3>Something is not right...</h3>
                <p>Please make sure all information is correct...</p>
    <%--            <p>or</p>--%>
    <%--            <p id="error"></p>--%>
                <a href="${pageContext.request.contextPath}/donate" class="href-dotted">Go Back!</a>
            </div>
        </div>


        <script>
            $(document).ready(function() {

                var processingDonation = false,
                    recurring = false;

                var $amount = $('.amount'),
                    $amountInput = $('#amount-input'),
                    $options = $('.option'),
                    $durations = $('.duration'),
                    $donateButton = $('#donate-button'),
                    $donationOptions = $('#donation-options'),
                    $makeDonationContainer = $('#make-donation-container'),
                    $custom = $('#custom'),
                    $fiver = $('#fiver'),
                    $success = $('#success'),
                    $contributionType = $('#contribution-type'),
                    $live = $('.live');

                var $organizationId = $('#organization-id'),
                    $creditCard = $('#credit-card'),
                    $expMonth = $('#exp-month'),
                    $expYear = $('#exp-year'),
                    $cvc = $('#cvc'),
                    $email = $('#email'),
                    $modal = $('#modal'),
                    $processing = $("#processing");

                var $username = $('#username'),
                    $password = $('#password'),
                    $organization = $('#organization'),
                    $donationAmount = $('#donation-amount');


                $custom.focus(function(){
                    $custom.val('');
                    $custom.addClass('active')
                })

                $custom.mouseleave(function(){
                    if($custom.val() == ''){
                        $custom.attr('placeholder', 'Other Amount')
                    }
                    if($custom.val() != ''){
                        $custom.val(pad($custom.val()))
                        $amount.html('Donate $' + pad($custom.val()))
                        $amountInput.val($custom.val())
                    }
                })

                $custom.change(function(){
                    var value = $custom.val()
                    if(!isNaN(value)){
                        $custom.val(pad($custom.val()))
                        $amount.html('Donate $' + pad(value))
                        $amountInput.val(value)
                    }else{
                        alert('Please enter a valid amount');
                    }
                })

                $durations.click(function(){
                    $durations.removeClass('retro').addClass('sky')
                    $durations.removeClass('active')
                    $(this).addClass('active').removeClass('sky')
                    if($(this).attr('data-recurring') == 'true'){
                        $contributionType.html('Monthly Donation')
                        recurring = true
                    }else{
                        $contributionType.html('One-Time Donation')
                        recurring = false
                    }
                })

                $options.click(function (evnt) {
                    var $target = $(evnt.target)
                    if(!$target.hasClass('super')) {

                        $options.removeClass('active')
                        $target.toggleClass('active')
                        var amount = $target.attr('data-amount')

                        if(amount != '' &&
                                amount != 0 &&
                                    !isNaN(amount)) {
                            $amount.html('Donate $' + amount)
                            $amountInput.val(amount)
                        }
                    }

                    $makeDonationContainer.fadeIn(300);
                })


                $donateButton.click(function(){
                    if(!processingDonation &&
                            isValidForm()){

                        $processing.show()
                        $modal.show()
                        // $live.hide()

                        processingDonation = true;

                        var raw = getRaw()
                        var data = JSON.stringify(raw)

                        $.ajax({
                            method: 'post',
                            url: '${pageContext.request.contextPath}/donation/make',
                            data: data,
                            contentType: "application/json",
                            success: function(data){
                                console.log(data);
                                $processing.hide()
                                if(data.processed){
                                    if('cleanPassword' in data.user){
                                        $username.html(data.user.username)
                                        $password.html(data.user.cleanPassword)
                                    }
                                    if(!('cleanPassword' in data.user)){
                                        $username.parents('p').hide()
                                        $password.parents('p').hide()
                                    }
                                    if('organization' in data){
                                        $organization.html(data.organization.name)
                                    }
                                    $donationAmount.html('$' + pad(data.amount.toString()))
                                    $success.fadeIn(100)
                                }else{
                                    $('#error').html(data.status)
                                    $('#error-container').show()
                                }
                            },
                            error: function(e){
                                $processing.hide()
                                console.log('...', e)
                            }
                        })
                    }
                })

                var getRaw = function(){
                    return {
                        "amount" : $amountInput.val().replace(/ /g,''),
                        "creditCard": $creditCard.val().replace(/ /g,''),
                        "expMonth" : $expMonth.val().replace(/ /g,''),
                        "expYear" : $expYear.val().replace(/ /g,''),
                        "cvc" : $cvc.val().replace(/ /g,''),
                        "email" : $email.val().replace(/ /g,''),
                        "recurring" : recurring,
                        "organizationId" : $organizationId.val() != '' ? $organizationId.val().replace(/ /g,'') : 0
                    };
                }

                var isValidForm = function(){
                    if($amountInput.val() == ''){
                        alert('Please select an option or enter a custom amount?')
                        return false
                    }
                    if($creditCard.val() == ''){
                        alert('Please enter a valid credit card #')
                        return false
                    }
                    if($expMonth.val() == ''){
                        alert('Please enter a valid expiration month')
                        return false
                    }
                    if($expYear.val() == ''){
                        alert('Please enter a valid expiration year')
                        return false
                    }
                    if($cvc.val() == ''){
                        alert('Please enter a valid cvc')
                        return false
                    }
                    if($email.val() == ''){
                        alert('Please enter a valid email address')
                        return false
                    }
                    return true
                }

                var pad = function(value){
                    var dollar = value
                    var splits = value.split(".")
                    if(splits != 'undefined'){
                        if(splits.length > 1){
                            if(splits[1].length <= 1){
                                dollar = addZero(value)
                            }
                            if(splits[1].length == 2){
                                return dollar
                            }
                        }else{
                            return dollar + ".00";
                        }
                    }else{
                        return dollar + ".00";
                    }
                    return dollar
                }

                var addZero = function(value){
                    value = value + "0";
                    return pad(value)
                }

                $fiver.click()
            })
        </script>

    </c:if>


</body>
</html>
