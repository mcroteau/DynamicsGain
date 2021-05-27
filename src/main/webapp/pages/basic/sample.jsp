<html>
<head>
    <title>Sample Plugin</title>
</head>
<body>


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

<script src="https://code.jquery.com/jquery-3.5.1.min.js"
        integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0="
        crossorigin="anonymous"></script>

<script>
    var url = 'http://sandbox.dynamicsgain.org/z/donation/make';

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

</body>
</html>

