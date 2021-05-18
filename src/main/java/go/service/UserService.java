package go.service;

import go.Spirit;
import go.model.Prospect;
import go.model.ProspectActivity;
import go.model.Role;
import go.model.User;
import go.repo.ProspectRepo;
import go.repo.RoleRepo;
import go.repo.UserRepo;
import eco.m1.annotate.Inject;
import eco.m1.annotate.Service;
import eco.m1.data.RequestData;
import xyz.goioc.Parakeet;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

@Service
public class UserService {

    @Inject
    RoleRepo roleRepo;

    @Inject
    UserRepo userRepo;

    @Inject
    ProspectRepo prospectRepo;

    @Inject
    AuthService authService;


    private String getPermission(String id){
        return Spirit.USER_MAINTENANCE + id;
    }

    public void setPretty(ProspectActivity prospectActivity){
        try {
            Prospect prospect = prospectRepo.get(prospectActivity.getProspectId());
            prospectActivity.setProspectName(prospect.getName());
            SimpleDateFormat format = new SimpleDateFormat(Spirit.DATE_TIME);
            Date date = format.parse(Long.toString(prospectActivity.getTaskDate()));

            SimpleDateFormat formatter = new SimpleDateFormat(Spirit.DATE_PRETTY);
            String pretty = formatter.format(date);
            prospectActivity.setPrettyTime(pretty);
        }catch (Exception ex){}
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
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            return "[redirect]/";
        }

        List<ProspectActivity> prospectActivities = prospectRepo.getActivities();
        prospectActivities.stream().forEach((prospectActivity -> setPretty(prospectActivity)));

        User user = userRepo.get(id);

        data.put("user", user);
        data.put("prospectActivities", prospectActivities);

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
            userRepo.save(user);

            User savedUser = userRepo.getByUsername(user.getUsername());
            Role defaultRole = roleRepo.find(Spirit.USER_ROLE);

            userRepo.saveUserRole(savedUser.getId(), defaultRole.getId());
            String permission = getPermission(Long.toString(savedUser.getId()));
            userRepo.savePermission(savedUser.getId(), permission);


        }catch(Exception e){
            e.printStackTrace();
            data.put("message", "Will you contact us? Email us with the subject, support@amadeus.social. Our programmers missed something. Gracias!");
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
