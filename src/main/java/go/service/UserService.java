package go.service;

import com.stripe.Stripe;
import eco.m1.annotate.Inject;
import eco.m1.annotate.Prop;
import eco.m1.annotate.Service;
import eco.m1.data.RequestData;
import go.Spirit;
import go.model.*;
import go.repo.DonationRepo;
import go.repo.OrganizationRepo;
import go.repo.RoleRepo;
import go.repo.UserRepo;
import xyz.goioc.Parakeet;

import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Service
public class UserService {

    @Prop("stripe.apiKey")
    String apiKey;

    @Inject
    UserRepo userRepo;

    @Inject
    DonationRepo donationRepo;

    @Inject
    OrganizationRepo organizationRepo;

    @Inject
    AuthService authService;


    private String getPermission(String id){
        return Spirit.USER_MAINTENANCE + id;
    }

    public String getUsers(RequestData data){
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        if(!authService.isAdministrator()){
            data.put("message", "You must be a super user in order to access accounts.");
            return "[redirect]/";
        }

        List<User> users = userRepo.findAll();
        data.put("users", users);

        return "/pages/user/index.jsp";
    }

    public String getEditUser(Long id, RequestData data){
        String permission = getPermission(Long.toString(id));
        if(!authService.hasPermission(permission)){
            return "[redirect]/";
        }

        User user = userRepo.get(id);
        List<Donation> donations = donationRepo.getList(user.getId());
        List<Charge> charges = new ArrayList<>();
        List<Subscription> subscriptions = new ArrayList<>();

        for(Donation donation: donations) {

            Stripe.apiKey = apiKey;

            Organization storedOrganization = null;
            if(donation.getOrganizationId() != null) {
                storedOrganization = organizationRepo.get(donation.getOrganizationId());
            }

            if(donation.getChargeId() != null &&
                    !donation.getChargeId().equals("null")) {
                Charge charge = new Charge();
                charge.setAmount(donation.getAmount());
                charge.setId(donation.getId());
                charge.setStripeId(donation.getChargeId());
                charge.setDonationDate(donation.getPrettyDate());
                if(storedOrganization != null){
                    charge.setOrganization(storedOrganization);
                }
                charges.add(charge);
            }

            System.out.println("subscription : '" + donation.getSubscriptionId() + "'");

            if(donation.getSubscriptionId() != null &&
                    !donation.getSubscriptionId().equals("null")) {
                System.out.println("subscription 2 : " + donation.getSubscriptionId());

                Subscription subscription = new Subscription();
                subscription.setAmount(donation.getAmount());
                subscription.setId(donation.getId());
                subscription.setStripeId(donation.getSubscriptionId());
                subscription.setDonationDate(donation.getPrettyDate());
                if(storedOrganization != null) {
                    subscription.setOrganization(storedOrganization);
                }
                if(donation.isCancelled()){
                    subscription.setCancelled(true);
                }
                subscriptions.add(subscription);

            }
        }

        data.put("charges", charges);
        data.put("subscriptions", subscriptions);
        data.put("user", user);

        return "/pages/user/edit.jsp";
    }


    public String editPassword(Long id, RequestData data) {

        String permission = getPermission(Long.toString(id));
        if(!authService.isAdministrator() ||
                !authService.hasPermission(permission)){
            return "[redirect]/";
        }

        User user = userRepo.get(id);
        data.put("user", user);
        return "/pages/user/password.jsp";
    }


    public String updatePassword(User user, RequestData data) {

        String permission = getPermission(Long.toString(user.getId()));
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            return "[redirect]/";
        }

        if(user.getPassword().length() < 7){
            data.put("message", "Passwords must be at least 7 characters long.");
            return "[redirect]/signup";
        }

        if(!user.getPassword().equals("")){
            user.setPassword(Parakeet.dirty(user.getPassword()));
            userRepo.updatePassword(user);
        }

        data.put("message", "password successfully updated");
        Long id = authService.getUser().getId();
        return "[redirect]/user/edit_password/" + id;

    }

    public String deleteUser(Long id, RequestData data) {
        if(!authService.isAdministrator()){
            data.put("message", "You don't have permission");
            return "[redirect]/";
        }

        data.put("message", "Successfully deleted user");
        return "[redirect]/admin/users";
    }

    public String register(HttpServletRequest req, RequestData data) {


        Enumeration<String> parameters = req.getParameterNames();
        while(parameters.hasMoreElements()){
            System.out.println(parameters.nextElement());
        }
        System.out.println("M1 Starter " + req);
        String username = req.getParameter("username");
        String rawPassword = req.getParameter("password");

        if(username == null ||
                username.equals("")){
            data.put("message", "please enter a username.");
            return "[redirect]/signup";
        }

        User existingUser = userRepo.getByUsername(username);
        if(existingUser != null){
            data.put("message", "User exists with same username.");
            return "[redirect]/signup";
        }

        if(rawPassword == null ||
                rawPassword.equals("")) {
            data.put("message", "Password cannot be blank");
            return "[redirect]/signup";
        }

        if(rawPassword.length() < 7){
            data.put("message", "Password must be at least 7 characters long.");
            return "[redirect]/signup";
        }

        String passwordHashed = Parakeet.dirty(rawPassword);

        try{
            User user = new User();

            user.setUsername(username);
            user.setPassword(passwordHashed);
            user.setDateCreated(Spirit.getDate());
            User savedUser = userRepo.save(user);

            userRepo.saveUserRole(savedUser.getId(), Spirit.DONOR_ROLE);

            User storedUser = userRepo.getByUsername(user.getUsername());
            userRepo.saveUserRole(storedUser.getId(), Spirit.DONOR_ROLE);

            String permission = getPermission(Long.toString(savedUser.getId()));
            userRepo.savePermission(savedUser.getId(), permission);


        }catch(Exception e){
            e.printStackTrace();
            data.put("message", "Will you email with the subject, support@gospirit.xyz. Our programmers missed something. Gracias!");
            return("[redirect]/signup");
        }

        if(!authService.signin(username, rawPassword)) {
            data.put("message", "Thank you for registering. We hope you enjoy!");
            return "[redirect]/";
        }

        req.getSession().setAttribute("username", username);
        req.getSession().setAttribute("userId", authService.getUser().getId());

        return "[redirect]/";
    }

    public String sendReset(RequestData data, HttpServletRequest req) {

        try {
            String username = req.getParameter("username");
            User user = userRepo.getByUsername(username);
            if (user == null) {
                data.put("message", "Unable to find user.");
                return ("[redirect]/user/reset");
            }

            String resetUuid = Spirit.getString(13);
            user.setUuid(resetUuid);
            userRepo.updateUuid(user);

            StringBuffer url = req.getRequestURL();

            String[] split = url.toString().split(req.getContextPath());
            String httpSection = split[0];

            String resetUrl = httpSection + req.getContextPath() + "/user/confirm?";
            String params = "username=" + URLEncoder.encode(user.getUsername(), "utf-8") + "&uuid=" + resetUuid;
            resetUrl += params;

            String body = "<p>Reset password</p>" +
                    "<p><a href=\"" + resetUrl + "\">" + resetUrl + "</a></p>";


        }catch(Exception e){
            e.printStackTrace();
        }

        return "/pages/user/send.jsp";
    }

    public String confirm(RequestData data, HttpServletRequest req) {

        String uuid = req.getParameter("uuid");
        String username = req.getParameter("username");

        User user = userRepo.getByUsernameAndUuid(username, uuid);
        if (user == null) {
            data.put("error", "Unable to locate user.");
            return "[redirect]/user/reset";
        }

        data.put("user", user);
        return "/pages/user/confirm.jsp";
    }

    public String resetPassword(Long id, RequestData data, HttpServletRequest req) {

        User user = userRepo.get(id);
        String uuid = req.getParameter("uuid");
        String username = req.getParameter("username");
        String rawPassword = req.getParameter("password");

        if(rawPassword.length() < 7){
            data.put("message", "Passwords must be at least 7 characters long.");
            return "[redirect]/user/confirm?username=" + username + "&uuid=" + uuid;
        }

        if(!rawPassword.equals("")){
            String password = Parakeet.dirty(rawPassword);
            user.setPassword(password);
            userRepo.updatePassword(user);
        }

        data.put("message", "Password successfully updated");
        return "/pages/user/success.jsp";
    }

    public String updateUser(Long id, RequestData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        String permission = getPermission(Long.toString(id));
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            return "[redirect]/";
        }

        User user = userRepo.get(id);
        String phone = req.getParameter("phone").trim();
        String username = req.getParameter("username").trim();

        user.setPhone(phone);
        user.setUsername(username);

        userRepo.update(user);
        data.put("message", "Successfully updated user.");
        return "[redirect]/users/edit/" + id;
    }
}
