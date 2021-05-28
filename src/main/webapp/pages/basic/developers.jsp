<html>
<head>
    <title>Developers API</title>
</head>
<body>

    <h1>Developers API</h1>

    <p>We have an API in that allows you to integrate
    Spirit <strong>+Gain's</strong> donations processor so you can make
        donations to the organization or shelter you love on your website or
        through your android app or ios app.
        All you need is the Organization's Id for the system.</p>

    <h2 style="margin-top:50px;">Making a Donation Request</h2>

    <p class="center">Perform a <strong>POST</strong> request to the following endpoint.</p>
    <p class="yellow inline">https://www.dynamicsgain.org/z/donation/make</p>
    <p class="center information">sample data payload below</p>
    <pre style="font-family:roboto-slab !important;display:inline-block;text-align:left; padding:20px; background: #f1f5f5;">
    {
        "creditCard": 4242424242424242,
        "expMonth" : 12,
        "expYear" : 2072,
        "cvc" : 123,
        "amount" : 123,
        "email" : croteau.mike@gmail.com,
        "recurring" : true|false,
        "location" : 1
    }</pre>

    <style>
        p{
            text-align: left;
        }
        ul li{
            list-style: none;
        }
    </style>
    <h4>Payload definitions:</h4>
    <ul style="text-align: left;margin-left:30px;">
        <li><strong>creditCard</strong>: The donors credit card number, no spaces, dashes or special characters.</li>
        <li><strong>expMonth</strong> : The donors credit card expiration month, two digits.</li>
        <li><strong>expYear</strong> : The donors credit card expiration year, 4 digits.</li>
        <li><strong>cvc</strong> : The donors credit card cvc number, 3 digits.</li>
        <li><strong>amount</strong> : The amount to be donated, may include decimal but not required.</li>
        <li><strong>email</strong> : The donors email address, an account will be created for them. Their username and usable password for our system will be sent in the response named "cleanPassword".</li>
        <li><strong>recurring</strong> : true|false if recurring monthly donation</li>
        <li><strong>location (optional)</strong> : The Id of the Charitable Organization or Homeless Shelter that you would like to donate to on the system.
            Location is used throughout our system to reference a Homeless Shelter or Charitable Organization.
        If you leave blank you can make a donation to Spirit +Gain instead.</li>
    </ul>

    <p class="center">Sample Response:</p>
    <pre style="margin:0px;font-family:roboto-slab !important;display:inline-block;text-align:left; padding:20px; background: #f1f5f5;">
{
        "id":3,
        "amount":5,
        "chargeId":"ch_1IMiSxFMDPZBpdm3BTkZtNMn",
        or
        "subscriptionId":"sub_IygsY8XLranuNy",
        "userId":1,
        "locationId":1,
        "processed":true,
        "status":"Successfully processed donation!",
        "user":{
            "id":1,
            "username":"croteau.mike@gmail.com",
            "password":"5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8",
            "cleanPassword" : "6RevZZK"
            "dateCreated":20210219000000,
            "stripeUserId":"cus_Iyfoxc5boHkxTf",
        },
        "location":{
            "id":1,
            "userId":0,
            "townId":1,
            "name":"Rescue Mission",
            "needs":"Shoes, Socks, Jackets, Laptops, Prepaid Phones",
            "count":0,
            "locationUri":"rescuemission0",
            "description":"Helping at-risk and homeless families in Clark
                County achieve sustainable housing and independence through a
                compassionate, community-based response.",
            "accounts":[],
            "counts":[]
        }
}</pre>

    <h2 style="margin-top:50px;">What if they want to cancel a monthly donation?</h2>

    <p class="center">Perform a <strong>DELETE</strong> request to the following endpoint.</p>
    <p class="center yellow inline">https://www.dynamicsgain.org/z/donation/cancel/{{subscription_id}}</p>
    <p class="center information">or if they made a donation to an Charitable Organization or Homeless shelter.</p>
    <p class="center yellow inline">https://www.dynamicsgain.org/z/donation/cancel/{{location_id}}/{{subscription_id}}</p>

    <h4>Path Parameters</h4>

    <ul style="text-align: left;margin-left:30px;">
        <li><strong>location_id</strong> : The Id of the Charitable Organization or Homeless Shelter in our system. If they are in our system you should see a Location Id on the Organizations page</li>
        <li><strong>subscription_id</strong> : The Id of the monthly donation named "subscriptionId" in the initial donation response.</li>
    </ul>

    <h2 style="margin-top:50px;">How easy is it?</h2>
    <p class="center">Here is a pre-made for implementation.</p>

    <h4>The Htlm form</h4>
    <textarea disabled="true" style="font-family: Courier !important; font-size: 13px; height:500px;">

<div id="dynamics-form">

    <input type="hidden" value="" id="location"/>

    <label>amount</label>
    <input type="number" id="amount" placeholder="123"/>

    <label>credit card</label>
    <input type="number" value="4242424242424242" id="credit-card" name="credit-card" placeholder="4242424242424242" maxlength="16"/>

    <div class="cc-details">
        <label>month</label>
        <input type="number" id="exp-month"name="exp-month" placeholder="09" maxlength="2"/>
    </div>

    <div class="cc-details">
        <label>year</label>
        <input type="number" id="exp-year" name="exp-year" placeholder="2072" maxlength="4"/>
    </div>

    <div class="cc-details">
        <label>cvc</label>
        <input type="number" id="cvc" name="cvc" placeholder="123" maxlength="3"/>
    </div>

    <label>Email</label>
    <input type="text" id="email" value="" placeholder="mail@dynamicsgain.org"/>

    <input type="checkbox" id="recurring"/>

    <input type="button" id="donate" value="Donate +"/>

</div>

    </textarea>


    <h4>jQuery</h4>
    <textarea disabled="true" style="font-family: Courier !important; font-size: 13px; height:1004px;">
<script src="https://code.jquery.com/jquery-3.5.1.min.js"
        integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0="
        crossorigin="anonymous"></script>

<script>
    var url = 'http://www.dynamicsgain.org/z/donation/make';

    var processing = false,
        recurring = false;

    var $creditCard = $('#credit-card'),
        $expMonth = $('#exp-month'),
        $expYear = $('#exp-year'),
        $cvc = $('#cvc'),
        $amount = $('#amount'),
        $email = $('#email'),
        $recurring = $('#recurring'),
        $location = $('#location'),
        $donate = $('#donate');

    $donate.click(function(){
        if(!processing) {
            var raw = getData();
            var data = JSON.stringify(raw)
            $.ajax({
                method: 'post',
                url: url,
                data: data,
                contentType: "application/json",
                success: function (resp) {
                    var json = JSON.parse(resp)
                    if (json.processed) {
                        //Do something neat!
                    }
                    if (!json.processed) {
                        alert(json.status)
                    }
                    processing = false;
                },
                error: function () {
                    processing = false;
                    alert('it did not process')
                }
            });
        }
    })

    var getData = function() {
        if ($recurring.is(':checked')) {
            recurring = true
        }
        if (!$recurring.is(':checked')){
            recurring = false;
        }
        /**
         Just in case you are not a regular expression guy like me
         .replace(/ /g,'') removes all white spaces, very important!
         No dollar signs allowed for amount.
         */
        return {
            "amount" : $amount.val().replace(/ /g,''),
            "creditCard": $creditCard.val().replace(/ /g,''),
            "expMonth" : $expMonth.val().replace(/ /g,''),
            "expYear" : $expYear.val().replace(/ /g,''),
            "cvc" : $cvc.val().replace(/ /g,''),
            "email" : $email.val().replace(/ /g,''),
            "recurring" : recurring,
            "location" : $location.val().replace(/ /g,'')
        };
    }
</script>
    </textarea>


    <h2 style="margin-top:50px;">What about testing?</h2>
    <p>We setup a dev environment for you to test the API on.
        So to make a donation on this system instead:</p>

    <p class="yellow inline">http://sandbox.dynamicsgain.org/z/donation/make</p>

    <p>Everything is the same. You want to make a donation to an Organization and
    they have to be in our system. So if they are not, you will have go through the
    process of getting them on our system, all we need is their Stripe key.</p>

    <h3>One Caveat</h3>
    <p>Do not use real credit card numbers to process donations on the dev system.
    Instead use:</p>

    <ul style="text-align: left;margin-left:30px">
        <li><strong>4242424242424242</strong> : for their credit card number</li>
        <li><strong>any year in the future</strong> : for expiration year</li>
        <li><strong>any month</strong> : for expiration month</li>
        <li><strong>any 3 digits</strong> : for cvc</li>
    </ul>

    <h2 style="margin-top:30px;">What do I need to test on your dev server?</h2>
    <p>There should be one Organization on the dev system to use
        with the id of 1. Yep... lol, 1. We didn't see the need to make the
        ids complicated. If you want to see the transactions, you will need to
        go a little further.
    </p>

    <h4>How much further?</h4>
    <p>You will need to setup a Stripe account. They are inexpensive and worth it,
        I make no money from them. Period. Stripe is a pay as you go service so
        nothing is charged unless a donation is made to you,
        then its cents on the dollar.</p>

    <p class="center">Here is the exact amount:</p>
    <h4 style="display:inline-block">2.9% + 30&cent; per charge</h4>

    <p class="center">so</p>
    <p class="center">$10 donation would cost 59&cent;</p>

    <br/>
    <a href="https://www.stripe.com" class="href-dotted" target="_blank">https://www.stripe.com</a>

    <a href="https://www.stripe.com" target="_blank">
        <img src="/z/assets/media/stripe.png" style="margin:auto;width:300px;display:block"/></a>


    <p class="left yellow">Again, we make nothing from a transaction! Just satisfaction that we
        may be helping people who are helping people! We hope we helped!</p>



    <h2 style="margin-top:50px;">Finally, is it secure?</h2>
    <p>All Requests are made over Https except for the dev environment,
        the credit card is not stored in our system, we rely on Stripe.com.
        They handle all the compliance stuff! They are awesome!</p>


</body>
</html>
