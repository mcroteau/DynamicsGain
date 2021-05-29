package go.service;

import com.stripe.Stripe;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import eco.m1.annotate.Inject;
import eco.m1.annotate.Prop;
import eco.m1.annotate.Service;
import eco.m1.data.RequestData;
import go.Spirit;
import go.model.*;
import go.repo.OrganizationRepo;
import go.repo.RoleRepo;
import go.repo.TownRepo;
import go.repo.UserRepo;
import go.support.Web;
import xyz.goioc.Parakeet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

@Service
public class OrganizationService {

    @Prop("stripe.apiKey")
    String apiKey;

    @Inject
    UserRepo userRepo;

    @Inject
    RoleRepo roleRepo;

    @Inject
    TownRepo townRepo;

    @Inject
    OrganizationRepo organizationRepo;

    @Inject
    AuthService authService;

    @Inject
    SmsService smsService;

    @Inject
    MailService mailService;

//    @Inject
//    SitemapService sitemapService;

    public String index(String uri, RequestData data) {
        Organization organization = organizationRepo.get(uri);
        data.put("organization", organization);
        return "/pages/organization/index.jsp";
    }

    public String create(RequestData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        if(!authService.isAdministrator() &&
                !authService.hasRole(Spirit.SUPER_ROLE)){
            return "[redirect]/";
        }
        List<Town> towns = townRepo.getList();
        data.put("towns", towns);
        return "/pages/organization/create.jsp";
    }

    public String getOrganizations(RequestData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        if(!authService.isAdministrator() &&
                !authService.hasRole(Spirit.SUPER_ROLE)){
            return "[redirect]/";
        }

        List<Organization> organizations = organizationRepo.getList();
        data.put("organizations", organizations);

        return "/pages/organization/list.jsp";
    }

    public String save(RequestData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        if(!authService.isAdministrator() &&
                !authService.hasRole(Spirit.SUPER_ROLE)){
            return "[redirect]/";
        }

        String name = req.getParameter("name");
        Long townId = Long.parseLong(req.getParameter("townId"));
        String uri = req.getParameter("uri");

        if(name == null ||
                name.equals("")){
            data.put("message", "Please give your town a name...");
            return "[redirect]/admin/towns/create";
        }

        Organization organization = new Organization();
        organization.setName(name);
        organization.setTownId(townId);
        organization.setUri(uri);

        Organization savedOrganization = organizationRepo.save(organization);

        return "[redirect]/admin/organizations/edit/" + savedOrganization.getId();
    }

    public String getEdit(Long id, RequestData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        if(!authService.isAdministrator() &&
                !authService.hasRole(Spirit.SUPER_ROLE)){
            data.put("message", "You do not have permission to this organization");
            return "[redirect]/ownership?id=" + id;
        }

        List<Town> towns = townRepo.getList();
        Organization organization = organizationRepo.get(id);

        data.put("towns", towns);
        data.put("organization", organization);

        return "/pages/organization/edit.jsp";
    }

    public String update(Long id, RequestData data, HttpServletRequest req) throws Exception {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        if(!authService.isAdministrator() &&
                !authService.hasRole(Spirit.SUPER_ROLE)){
            return "[redirect]/";
        }

        Organization organization = organizationRepo.get(id);

        String name = req.getParameter("name");
        Long townId = Long.parseLong(req.getParameter("townId"));
        String description = req.getParameter("description");
        String uri = req.getParameter("uri");
        String latitude = req.getParameter("latitude");
        String longitude = req.getParameter("longitude");

        if(name == null || name.equals("")){
            data.put("message", "Please give your organization a name...");
            return "[redirect]/admin/organizations/edit/" + id;
        }

        organization.setName(name);
        organization.setTownId(townId);
        organization.setDescription(description);
        organization.setUri(uri);
        organization.setLatitude(latitude);
        organization.setLongitude(longitude);

//        List<Organization> organizations = organizationRepo.getList();
//        try {
//            sitemapService.writeOrganizations(organizations);
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }

        organizationRepo.update(organization);

        data.put("message", "Successfully updated organization");
        return "[redirect]/admin/organizations/edit/" + id;
    }

    public String delete(Long id, RequestData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        if(!authService.isAdministrator() &&
                !authService.hasRole(Spirit.SUPER_ROLE)){
            return "[redirect]/";
        }

        organizationRepo.delete(id);
        data.put("message", "Successfully deleted organization.");

        return "[redirect]/admin/organizations";
    }


    public String takeOwnership(HttpServletRequest req, RequestData data) {
        if(req.getParameter("id") != null &&
                !req.getParameter("id").equals("")) {
            Long id = Long.parseLong(req.getParameter("id"));
            Organization organization = organizationRepo.get(id);
            data.put("organization", organization);
        }
        List<Organization> organizations = organizationRepo.getList();
        data.put("organizations", organizations);
        return "/pages/basic/ownership.jsp";
    }

    public String ownership(HttpServletRequest req, RequestData data) {
        OwnershipRequest ownershipRequest = (OwnershipRequest) Web.hydrate(req, OwnershipRequest.class);
        if(ownershipRequest.getName() == null ||
                ownershipRequest.getName().equals("")){
            data.put("message", "Please enter a contact name");
            return "[redirect]/ownership";
        }
        if(!Spirit.isValidMailbox(ownershipRequest.getEmail())){
            data.put("message", "Please enter a valid email address");
            return "[redirect]/ownership";
        }
        if(ownershipRequest.getPhone() == null ||
                ownershipRequest.getPhone().equals("")){
            data.put("message", "Please enter a contact phone");
            return "[redirect]/ownership";
        }

        ownershipRequest.setDateRequested(Spirit.getDate());
        organizationRepo.saveRequest(ownershipRequest);

        data.put("message", "Successfully sent request");
        data.put("requested", true);

        return "[redirect]/ownership";
    }

    public String requests(HttpServletRequest req, RequestData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        if(!authService.isAdministrator() &&
                !authService.hasRole(Spirit.SUPER_ROLE)){
            return "[redirect]/";
        }

        List<OwnershipRequest> reqs = organizationRepo.getRequests();
        data.put("reqs", reqs);
        return "/pages/organization/requests.jsp";
    }

    public String approveRequest(Long id, RequestData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        if(!authService.isAdministrator() &&
                !authService.hasRole(Spirit.SUPER_ROLE)){
            return "[redirect]/";
        }

        OwnershipRequest req = organizationRepo.getRequest(id);
        req.setApproved(true);
        organizationRepo.updateRequest(req);

        User user = new User();
        String dirty = Parakeet.dirty("bluebird");
        user.setPassword(dirty);
        user.setUsername(req.getEmail());
        user.setPhone(req.getPhone());
        user.setOrganizationId(id);
        User savedUser = userRepo.save(user);


        userRepo.saveUserRole(savedUser.getId(), Spirit.DONOR_ROLE);
        userRepo.saveUserRole(savedUser.getId(), Spirit.CHARITY_ROLE);

        String userPermission = Spirit.USER_MAINTENANCE + savedUser.getId();
        userRepo.savePermission(savedUser.getId(), userPermission);

        String orgPermission = Spirit.ORGANIZATION_MAINTENANCE + id;
        userRepo.savePermission(savedUser.getId(), orgPermission);

        data.put("message", "Successfully approved request.");
        return "[redirect]/admin/ownership/requests";
    }

    public String activateAccount(Long id, RequestData data, HttpServletRequest req, HttpServletResponse resp) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        if(!authService.hasRole(Spirit.CHARITY_ROLE)){
            data.put("message", "You do not have permission to access this organization");
            return "[redirect]/";
        }

        User user = userRepo.get(id);

        AccountCreateParams accountParams =
                AccountCreateParams.builder()
                        .setType(AccountCreateParams.Type.STANDARD)
                        .build();

        try {

            Stripe.apiKey = apiKey;

            Account account = Account.create(accountParams);

            AccountLinkCreateParams linkParams =
                    AccountLinkCreateParams.builder()
                            .setAccount(account.getId())
                            .setRefreshUrl("https://gospirit.xyz/reauth")
                            .setReturnUrl("https://gospirit.xyz/stripe/return")
                            .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                            .build();

            AccountLink accountLink = AccountLink.create(linkParams);

            user.setStripeAccountId(account.getId());
            user.setCharity(true);

            Organization organization = organizationRepo.get(user.getOrganizationId());
            organization.setStripeAccountId(account.getId());
            organizationRepo.update(organization);

            userRepo.update(user);

            PrintWriter pw = resp.getWriter();
            resp.sendRedirect(accountLink.getUrl());
            pw.close();

            return "";

        }catch(Exception ex){
            ex.printStackTrace();
            data.put("message", "Something went wrong, will you contact us and let us know?");
            return "[redirect]/";
        }

    }
}
