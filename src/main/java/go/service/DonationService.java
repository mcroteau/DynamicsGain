package go.service;

import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.Subscription;
import com.stripe.net.RequestOptions;
import com.stripe.param.SubscriptionCreateParams;
import eco.m1.annotate.Inject;
import eco.m1.annotate.Prop;
import eco.m1.annotate.Service;
import eco.m1.data.RequestData;
import go.Spirit;
import go.model.*;
import go.repo.DonationRepo;
import go.repo.OrganizationRepo;
import go.repo.StripeRepo;
import go.repo.UserRepo;
import xyz.goioc.Parakeet;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class DonationService {

    Gson gson = new Gson();

    @Inject
    UserRepo userRepo;

    @Inject
    OrganizationRepo organizationRepo;

    @Inject
    StripeRepo stripeRepo;

    @Inject
    DonationRepo donationRepo;

    @Inject
    AuthService authService;

    @Inject
    SmsService smsService;

    @Prop("stripe.apiKey")
    String apiKey;


    public String getUserPermission(String id){
        return Spirit.USER_MAINTENANCE + id;
    }

    public String index(RequestData data) {
        data.put("inDonateMode", true);
        return "/pages/donate/index.jsp";
    }

    public String organization(Long id, RequestData data) {
        Organization organization = organizationRepo.get(id);
        data.put("organization", organization);
        if(organization.getStripeAccountId() != null &&
                !organization.getStripeAccountId().equals("null")) {
            System.out.println("get stripe id: " + organization.getStripeAccountId());
            data.put("inDonateMode", true);
        }
        return "/pages/donate/index.jsp";
    }

    public Donation make(RequestData data, HttpServletRequest req){

        Donation donation = new Donation();
        String reqBody = "";

        try{
            reqBody = req.getReader().lines().collect(Collectors.joining());
        }catch (Exception ex){ ex.printStackTrace();
            donation.setStatus("Make sure all fields are complete. Organization must be 0 if an organization is not selected.");
            return donation;
        }

        System.out.println(reqBody);
        DonationInput donationInput = gson.fromJson(reqBody, DonationInput.class);

        System.out.println(donationInput);
        if(donationInput.getAmount() == null){
            donation.setStatus("$ amount not passed in, please give it another go!");
            return donation;
        }
        if(!Spirit.isValidMailbox(donationInput.getEmail())){
            donation.setStatus("Email is invalid, please give it another go!");
            return donation;
        }
        if(donationInput.getCreditCard().equals("")){
            donation.setStatus("Credit card is empty, please give it another go!");
            return donation;
        }
        if(donationInput.getExpMonth() == null){
            donation.setStatus("Expiration month is empty, please give it another go!");
            return donation;
        }
        if(donationInput.getExpYear() == null){
            donation.setStatus("Expiration year is empty, please give it another go!");
            return donation;
        }
        if(donationInput.getCvc().equals("")){
            donation.setStatus("Cvc is empty, please give it another go!");
            return donation;
        }

        System.out.println("made it... clear");

//        smsService.support("Go ~ " + donationInput.getEmail());

        try {

            Organization organization = null;
            if(donationInput.getOrganizationId() != null &&
                    donationInput.getOrganizationId() != 0){
                organization = organizationRepo.get(donationInput.getOrganizationId());
            }

            if(donationInput.getOrganizationId() != null &&
                    donationInput.getOrganizationId() == 0){
                donationInput.setOrganizationId(null);
                donation.setOrganizationId(null);
            }

            Stripe.apiKey = apiKey;
            User user = userRepo.getByUsername(donationInput.getEmail());
            String password = Spirit.getString(7);

            if (user == null) {
                user = new User();
                user.setUsername(donationInput.getEmail());
                user.setPassword(Parakeet.dirty(password));
                user.setDateCreated(Spirit.getDate());
                user = userRepo.save(user);
                user.setCleanPassword(password);
            }

            donation.setProcessed(false);
            donation.setAmount(donationInput.getAmount());
            donation.setUserId(user.getId());
            donation.setDonationDate(Spirit.getDate());
            if (donationInput.getOrganizationId() != null) {
                donation.setOrganizationId(donationInput.getOrganizationId());
            }

            donation = donationRepo.save(donation);
            donation.setStatus("hasn't processed yet...");

            Map<String, Object> card = new HashMap<>();
            card.put("number", donationInput.getCreditCard());
            card.put("exp_month", donationInput.getExpMonth());
            card.put("exp_year", donationInput.getExpYear());
            card.put("cvc", donationInput.getCvc());
            Map<String, Object> params = new HashMap<>();
            params.put("card", card);

            Token token = null;
            if(organization != null) {
                RequestOptions requestOptions = RequestOptions.builder()
                                .setStripeAccount(organization.getStripeAccountId())
                                .build();
                token = Token.create(params, requestOptions);
            }else{
                token = Token.create(params);
            }

            if (user.getStripeCustomerId() != null &&
                    !user.getStripeCustomerId().equals("")) {
                try {
                    Customer oldCustomer = Customer.retrieve(user.getStripeCustomerId());
                    oldCustomer.delete();
                } catch (Exception e) {
                    System.out.println("stale stripe customer id");
                }
            }

            Map<String, Object> customerParams = new HashMap<>();
            customerParams.put("email", donationInput.getEmail());
            customerParams.put("source", token.getId());

            Customer customer = null;
            if(organization != null) {
                RequestOptions requestOptions = RequestOptions.builder()
                                .setStripeAccount(organization.getStripeAccountId())
                                .build();
                customer = com.stripe.model.Customer.create(customerParams, requestOptions);
            }else{
                customer = com.stripe.model.Customer.create(customerParams);
            }

            user.setStripeCustomerId(customer.getId());
            userRepo.update(user);

            Long amountInCents = donationInput.getAmount().multiply(new BigDecimal(100)).longValue();
            System.out.println("amount -> " + amountInCents);

            String nickname = getDescription(donationInput, organization);
            System.out.println("nickname " + nickname);

            Boolean chargeSuccess = false;
            Boolean subscriptionSuccess = false;

            if (donationInput.isRecurring()) {

                DynamicsPrice storedPrice = stripeRepo.getPriceAmount(donationInput.getAmount());

                if (storedPrice != null) {

                    Price price =  com.stripe.model.Price.retrieve(storedPrice.getStripeId());
                    if (price == null) {
                        generateStripePrice(amountInCents, storedPrice, donation, organization);
                        storedPrice = stripeRepo.getPrice(storedPrice.getId());
                    }
                    subscriptionSuccess = createSubscription(donation, storedPrice, customer, organization);
                }

                if (storedPrice == null) {

                    DynamicsPrice dynamicsPrice = new DynamicsPrice();
                    dynamicsPrice.setAmount(donationInput.getAmount());
                    dynamicsPrice.setNickname(nickname);

                    Map<String, Object> productParams = new HashMap<>();
                    productParams.put("name", dynamicsPrice.getNickname());

                    com.stripe.model.Product stripeProduct = null;
                    if(organization != null) {
                        RequestOptions requestOptions = RequestOptions.builder().setStripeAccount(organization.getStripeAccountId()).build();
                        stripeProduct = com.stripe.model.Product.create(productParams, requestOptions);
                    }else{
                        stripeProduct = com.stripe.model.Product.create(productParams);
                    }

                    System.out.println(dynamicsPrice.getNickname() + " ::: " + stripeProduct.getId());

                    DynamicsProduct dynamicsProduct = new DynamicsProduct();
                    dynamicsProduct.setNickname(dynamicsPrice.getNickname());
                    dynamicsProduct.setStripeId(stripeProduct.getId());
                    DynamicsProduct savedProduct = stripeRepo.saveProduct(dynamicsProduct);

                    Price stripePrice = genStripeRecurringPrice(amountInCents, dynamicsPrice, stripeProduct, organization);
                    if (stripePrice == null) {
                        return donation;
                    }

                    dynamicsPrice.setStripeId(stripePrice.getId());
                    dynamicsPrice.setProductId(savedProduct.getId());
                    DynamicsPrice savedPrice = stripeRepo.savePrice(dynamicsPrice);

                    subscriptionSuccess = createSubscription(donation, savedPrice, customer, organization);

                }
            }

            if (!donationInput.isRecurring()) {

                Map<String, Object> chargeParams = new HashMap<>();
                chargeParams.put("amount", amountInCents);
                chargeParams.put("customer", customer.getId());
                chargeParams.put("card", token.getCard().getId());
                chargeParams.put("currency", "usd");

                com.stripe.model.Charge charge = null;
                if(organization != null) {
                    RequestOptions requestOptions = RequestOptions.builder().setStripeAccount(organization.getStripeAccountId()).build();
                    charge = com.stripe.model.Charge.create(chargeParams, requestOptions);
                }else{
                    charge = com.stripe.model.Charge.create(chargeParams);
                }

                donation.setChargeId(charge.getId());
                donationRepo.update(donation);

                user.setStripeCustomerId(customer.getId());
                userRepo.update(user);

                chargeSuccess = true;
            }

            if (chargeSuccess || subscriptionSuccess) {
                if (organization != null) {
                    donation.setOrganizationId(organization.getId());
                    donation.setOrganization(organization);
                }

                donation.setUser(user);
                donation.setProcessed(true);
                donation.setStatus("Successfully processed donation!");
                donationRepo.update(donation);
            }

            if(!chargeSuccess && !subscriptionSuccess){
                donation.setStatus("We need to adjust something. Nothing was charged. Please try again or contact someone");
            }

        }catch(StripeException ex){
            donation.setStatus(ex.getMessage());
        }

        return donation;
    }

    private Price generateStripePrice(Long amountInCents, DynamicsPrice storedPrice, Donation donation, Organization organization) throws StripeException {
        DynamicsProduct dynamicsProduct = stripeRepo.getProduct(storedPrice.getProductId());
        Map<String, Object> productParams = new HashMap<>();
        productParams.put("name", storedPrice.getNickname());

        com.stripe.model.Product stripeProduct = null;
        if(organization != null) {
            RequestOptions requestOptions = RequestOptions.builder().setStripeAccount(organization.getStripeAccountId()).build();
            stripeProduct = com.stripe.model.Product.create(productParams, requestOptions);
        }else{
            stripeProduct = com.stripe.model.Product.create(productParams);
        }


        dynamicsProduct.setStripeId(stripeProduct.getId());
        stripeRepo.updateProduct(dynamicsProduct);

        Map<String, Object> recurring = new HashMap<>();
        recurring.put("interval", "month");

        Map<String, Object> priceParams = new HashMap<>();
        priceParams.put("product", stripeProduct.getId());
        priceParams.put("unit_amount", amountInCents);
        priceParams.put("currency", "usd");
        priceParams.put("recurring", recurring);

        Price stripePrice = null;
        if(organization != null) {
            RequestOptions requestOptions = RequestOptions.builder().setStripeAccount(organization.getStripeAccountId()).build();
            stripePrice = Price.create(priceParams, requestOptions);
        }else{
            stripePrice = Price.create(priceParams);
        }
        storedPrice.setStripeId(stripePrice.getId());
        stripeRepo.updatePrice(storedPrice);

        return stripePrice;
    }

    private String getDescription(DonationInput donationInput, Organization organization){
        String reoccurring = donationInput.isRecurring() ? "Month" : "";
        String organizationName = "";
        if(organization != null){
            Organization storedOrganization = organizationRepo.get(donationInput.getOrganizationId());
            organizationName = storedOrganization.getName();
        }
        return "\\$" + donationInput.getAmount() + " " + reoccurring + " " +  organizationName;
    }


    private Price genStripeRecurringPrice(Long amountInCents, DynamicsPrice dynamicsPrice, Product stripeProduct, Organization organization) throws StripeException {
        Map<String, Object> recurring = new HashMap<>();
        recurring.put("interval", dynamicsPrice.getFrequency());

        Map<String, Object> priceParams = new HashMap<>();
        priceParams.put("product", stripeProduct.getId());
        priceParams.put("unit_amount", amountInCents);
        priceParams.put("currency", dynamicsPrice.getCurrency());
        priceParams.put("recurring", recurring);

        Price stripePrice = null;
        if(organization != null) {
            RequestOptions requestOptions = RequestOptions.builder().setStripeAccount(organization.getStripeAccountId()).build();
            stripePrice = Price.create(priceParams, requestOptions);
        }else{
            stripePrice = Price.create(priceParams);
        }

        return stripePrice;
    }

    private boolean createSubscription(Donation donation, DynamicsPrice dynamicsPrice, Customer customer, Organization organization) throws StripeException {

        SubscriptionCreateParams params = SubscriptionCreateParams.builder()
                .setCustomer(customer.getId())
                .addItem(SubscriptionCreateParams.Item.builder()
                        .setPrice(dynamicsPrice.getStripeId())
                        .build())
                .addExpand("latest_invoice.payment_intent")
                .build();
        Subscription subscription = null;
        if(organization != null) {
            RequestOptions requestOptions = RequestOptions.builder().setStripeAccount(organization.getStripeAccountId()).build();
            subscription = Subscription.create(params, requestOptions);
        }else{
            subscription = Subscription.create(params);
        }

        donation.setSubscriptionId(subscription.getId());
        donationRepo.update(donation);

        return true;
    }

    public String cancel(String subscriptionId){
        try{

            Stripe.apiKey = apiKey;
            com.stripe.model.Subscription subscription = com.stripe.model.Subscription.retrieve(subscriptionId);
            subscription.cancel();

            Donation donation = donationRepo.get(subscriptionId);
            donation.setCancelled(true);
            donationRepo.update(donation);

        }catch(Exception ex){
            ex.printStackTrace();
        }

        return Spirit.GO_FUTURES;
    }

    public String cleanup(){
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        if(!authService.isAdministrator()){
            return "[redirect]/";
        }
        try {

            Stripe.apiKey = apiKey;

            Map<String, Object> params = new HashMap<>();
            params.put("limit", 100);
            PlanCollection priceCollection = com.stripe.model.Plan.list(params);
            List<Plan> plans = priceCollection.getData();
            for(Plan plan: plans){
                plan.delete();
            }

            ProductCollection productCollection = com.stripe.model.Product.list(params);
            List<com.stripe.model.Product> products = productCollection.getData();
            for(com.stripe.model.Product product: products){
                product.delete();
            }

            CustomerCollection customerCollection = com.stripe.model.Customer.list(params);
            List<com.stripe.model.Customer> customers = customerCollection.getData();
            for(Customer customer: customers){
                customer.delete();
            }

            SubscriptionCollection subscriptionCollection = com.stripe.model.Subscription.list(params);
            List<com.stripe.model.Subscription> subscriptions = subscriptionCollection.getData();
            for(com.stripe.model.Subscription subscription: subscriptions){
                subscription.cancel();
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return "[redirect]/";
    }

    public String momentum(RequestData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        if(!authService.isAdministrator()){
            return "[redirect]/unauthorized";
        }
        List<Donation> donations = donationRepo.getList();
        BigDecimal sum = new BigDecimal(0);
        for(Donation donation: donations){
            sum = sum.add(donation.getAmount());
        }
        data.put("sum", sum);
        data.put("donations", donations);
        return "/pages/momentum/index.jsp";
    }
}
