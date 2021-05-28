package go.service;

import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
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
import go.support.Web;
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
        return "/pages/donate/index.jsp";
    }

    public Donation make(RequestData data, HttpServletRequest req){

        Donation donation = new Donation();
        String reqBody = "";

        try{
            reqBody = req.getReader().lines().collect(Collectors.joining());
        }catch (Exception ex){ ex.printStackTrace();}

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

        if(donationInput.getOrganization() != null &&
                donationInput.getOrganizationId() == null){
            donationInput.setOrganizationId(donationInput.getOrganization());
        }

        System.out.println("made it... clear");

        return donation;
    }

    private Price generateStripePrice(Long amountInCents, DynamicsPrice storedPrice, Donation donation) throws StripeException {
        DynamicsProduct dynamicsProduct = stripeRepo.getProduct(storedPrice.getProductId());
        Map<String, Object> productParams = new HashMap<>();
        productParams.put("name", storedPrice.getNickname());

        com.stripe.model.Product stripeProduct = com.stripe.model.Product.create(productParams);

        dynamicsProduct.setStripeId(stripeProduct.getId());
        stripeRepo.updateProduct(dynamicsProduct);

        Map<String, Object> recurring = new HashMap<>();
        recurring.put("interval", "month");

        Map<String, Object> priceParams = new HashMap<>();
        priceParams.put("product", stripeProduct.getId());
        priceParams.put("unit_amount", amountInCents);
        priceParams.put("currency", "usd");
        priceParams.put("recurring", recurring);

        Price stripePrice = Price.create(priceParams);
        storedPrice.setStripeId(stripePrice.getId());
        stripeRepo.updatePrice(storedPrice);

        return stripePrice;
    }

    private String getDescription(DonationInput donationInput){
        String reoccurring = donationInput.isRecurring() ? "Month" : "";
        String organization = "";
        if(donationInput.getOrganizationId() != null){
            Organization storedOrganization = organizationRepo.get(donationInput.getOrganizationId());
            organization = storedOrganization.getName();
        }
        return "$" + donationInput.getAmount() + " " + reoccurring + " " +  organization;
    }


    private Price genStripeRecurringPrice(Long amountInCents, DynamicsPrice dynamicsPrice, Product stripeProduct) throws StripeException {
        Map<String, Object> recurring = new HashMap<>();
        recurring.put("interval", dynamicsPrice.getFrequency());

        Map<String, Object> priceParams = new HashMap<>();
        priceParams.put("product", stripeProduct.getId());
        priceParams.put("unit_amount", amountInCents);
        priceParams.put("currency", dynamicsPrice.getCurrency());
        priceParams.put("recurring", recurring);

        Price stripePrice = Price.create(priceParams);
        return stripePrice;
    }

    private boolean createSubscription(Donation donation, DynamicsPrice dynamicsPrice, Customer customer) throws StripeException {
        Map<String, Object> itemParams = new HashMap<>();
        itemParams.put("price", dynamicsPrice.getStripeId());

        Map<String, Object> itemsParams = new HashMap<>();
        itemsParams.put("0", itemParams);

        Map<String, Object> subscriptionParams = new HashMap<>();
        subscriptionParams.put("customer", customer.getId());
        subscriptionParams.put("items", itemsParams);

        com.stripe.model.Subscription subscription = com.stripe.model.Subscription.create(subscriptionParams);

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

    public String cancel(Long organizationId, String subscriptionId){
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
