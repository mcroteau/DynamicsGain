package go.service;

import go.Spirit;
import go.model.*;
import go.repo.ActivityRepo;
import go.repo.EffortRepo;
import go.repo.StatusRepo;
import eco.m1.annotate.Inject;
import eco.m1.annotate.Service;
import eco.m1.data.RequestData;
import go.repo.ProspectRepo;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service
public class ProspectService {

    @Inject
    AuthService authService;

    @Inject
    ProspectRepo prospectRepo;

    @Inject
    StatusRepo statusRepo;

    @Inject
    EffortRepo effortRepo;

    @Inject
    ActivityRepo activityRepo;

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

    private ProspectActivity checkCorrectPhonesFormat(String phones, ProspectActivity prospectActivity) {
        if(phones == null ||
                phones.equals("")){
            return prospectActivity;
        }
        String[] phoneParts = phones.split(",");
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for(String phone: phoneParts){
            phone = phone.trim();
            if(!phone.startsWith("+1")){
                phone = "+1".concat(phone);
            }
            sb.append(phone);
            if(count < phoneParts.length){
                sb.append(",");
            }
            count++;
        }
        System.out.println("sb + " + sb.toString());
        prospectActivity.setPhones(sb.toString());
        return prospectActivity;
    }

    protected ProspectActivity setPhone(ProspectActivity activity){
        User user = authService.getUser();
        if(user.getPhone() != null &&
                !user.getPhone().equals("")){
            String phone = user.getPhone();
            if(phone.startsWith("+1")){
                activity.setPhones(phone);
            }else{
                activity.setPhones("+1" + phone);
            }
        }
        return activity;
    }

    public String searchScreen(RequestData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        List<ProspectActivity> prospectActivities = prospectRepo.getActivities();
        prospectActivities.stream().forEach((prospectActivity -> setPretty(prospectActivity)));
        data.put("prospectActivities", prospectActivities);

        Long prospectCount = prospectRepo.getCount();
        data.put("prospectCount", prospectCount);

        return "/pages/prospect/search.jsp";
    }

    public String getProspects(RequestData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        List<ProspectActivity> prospectActivities = prospectRepo.getActivities();
        prospectActivities.stream().forEach((prospectActivity -> setPretty(prospectActivity)));
        data.put("prospectActivities", prospectActivities);

        String query = req.getParameter("q");
        List<Prospect> prospects = prospectRepo.getResults(query);
        data.put("prospects", prospects);

        return "/pages/prospect/results.jsp";
    }

    public String index(Long id, RequestData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        List<ProspectActivity> prospectActivities = prospectRepo.getActivities();
        prospectActivities.stream().forEach((prospectActivity -> setPretty(prospectActivity)));
        data.put("prospectActivities", prospectActivities);

        List<Status> statuses = statusRepo.getList();
        Prospect prospect = prospectRepo.get(id);

        List<ProspectActivity> upcomingActivities = prospectRepo.getUncompletedActivities(id);
        upcomingActivities.stream().forEach((prospectActivity) -> setPretty(prospectActivity));
        Effort effort = effortRepo.getProspectEffort(prospect.getId(), false);

        data.put("effort", effort);
        data.put("upcomingActivities", upcomingActivities);
        data.put("statuses", statuses);
        data.put("prospect", prospect);
        return "/pages/prospect/index.jsp";
    }

    public String create(RequestData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        List<ProspectActivity> prospectActivities = prospectRepo.getActivities();
        prospectActivities.stream().forEach((prospectActivity -> setPretty(prospectActivity)));
        data.put("prospectActivities", prospectActivities);

        List<Status> statuses = statusRepo.getList();
        data.put("statuses", statuses);

        return "/pages/prospect/create.jsp";
    }

    public String save(RequestData data, HttpServletRequest req) {

        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        String name = req.getParameter("name");
        String phone = req.getParameter("phone");
        String contacts = req.getParameter("contacts");
        Long statusId = Long.parseLong(req.getParameter("status"));

        if(name.equals("")){
            data.put("message", "Please give your prospect a name...");
            return "[redirect]/prospects/create";
        }

        Prospect prospect = new Prospect();
        prospect.setName(name);
        prospect.setPhone(phone);
        prospect.setContacts(contacts);
        prospect.setStatusId(statusId);

        Prospect savedProspect = prospectRepo.save(prospect);

        data.put("message", "Successfully saved " + prospect.getName() + "!");
        return "[redirect]/prospects/" + savedProspect.getId();
    }

    public String getEdit(Long id, RequestData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        List<ProspectActivity> prospectActivities = prospectRepo.getActivities();
        prospectActivities.stream().forEach((prospectActivity -> setPretty(prospectActivity)));
        data.put("prospectActivities", prospectActivities);

        List<Status> statuses = statusRepo.getList();
        Prospect prospect = prospectRepo.get(id);
        Effort effort = effortRepo.getProspectEffort(prospect.getId(), false);

        data.put("effort", effort);
        data.put("statuses", statuses);
        data.put("prospect", prospect);

        return "/pages/prospect/edit.jsp";
    }

    public String update(RequestData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        Long id = Long.parseLong(req.getParameter("id"));
        String name = req.getParameter("name");
        String phone = req.getParameter("phone");
        String contacts = req.getParameter("contacts");

        if(name.equals("")){
            data.put("message", "Please give your prospect a name...");
            return "[redirect]/prospects/edit/" + id;
        }

        Prospect prospect = prospectRepo.get(id);
        prospect.setName(name);
        prospect.setPhone(phone);
        prospect.setContacts(contacts);
        prospectRepo.update(prospect);

        List<Status> statuses = statusRepo.getList();

        data.put("statuses", statuses);
        data.put("message", "Successfully updated prospect");
        return "[redirect]/prospects/edit/" + prospect.getId();
    }


    public String delete(Long id, RequestData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        if(!authService.isAdministrator()){
            return "[redirect]/unauthorized";
        }

        prospectRepo.deleteActivities(id);
        prospectRepo.delete(id);
        data.put("message", "Successfully deleted prospect.");

        return "[redirect]/prospects";
    }

    public String addActivity(Long id, RequestData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        List<ProspectActivity> prospectActivities = prospectRepo.getActivities();
        prospectActivities.stream().forEach((prospectActivity -> setPretty(prospectActivity)));
        data.put("prospectActivities", prospectActivities);

        List<Activity> activities = activityRepo.getList();
        Prospect prospect = prospectRepo.get(id);
        data.put("timezone", TimeZone.getDefault().getDisplayName());
        data.put("activities", activities);
        data.put("prospect", prospect);
        return "/pages/prospect_activity/index.jsp";
    }

    public String saveActivity(Long id, RequestData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        try {

            Long activityId = Long.parseLong(req.getParameter("activity-id"));
            Activity activity = activityRepo.get(activityId);
            Prospect prospect = prospectRepo.get(id);
            Effort effort = effortRepo.getProspectEffort(prospect.getId(), false);

            String date = req.getParameter("activity-date");
            String hour = Spirit.pad(req.getParameter("activity-hour").trim(), 2, "0");
            String minute = Spirit.pad(req.getParameter("activity-minute").trim(), 2, "0");

            String dateStr = date.concat(" ")
                    .concat(hour)
                    .concat(":").concat(minute);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date datePreParsed = format.parse(dateStr);

            Calendar cal = Calendar.getInstance();
            cal.setTime(datePreParsed);

            String timezone = req.getParameter("timezone");

            Date dateMillis = new Date();
            dateMillis.setTime(cal.getTimeInMillis());
            SimpleDateFormat sdf = new SimpleDateFormat(Spirit.DATE_FORMAT);

            Long taskDate = Long.parseLong(sdf.format(dateMillis));

            ProspectActivity prospectActivity = new ProspectActivity();
            prospectActivity.setTaskDate(taskDate);
            prospectActivity.setTimeZone(timezone);
            prospectActivity.setActivityId(activityId);
            prospectActivity.setProspectId(id);

            if(effort != null){
                prospectActivity.setEffortId(effort.getId());
            }

            setPhone(prospectActivity);

            prospectRepo.saveActivity(prospectActivity);

        }catch(Exception ex){
            ex.printStackTrace();
            data.put("message", "You may have entered in an incorrect time. Please try again");
            return "[redirect]/prospects/activity/add/" + id;
        }

        ProspectActivity savedActivity = prospectRepo.getLastInsertedActivity();
        data.put("message", "Successfully added sales action");
        return "[redirect]/prospects/activity/edit/"  + savedActivity.getId();
    }

    public String completeActivity(Long id, RequestData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        User user = authService.getUser();
        ProspectActivity prospectActivity = prospectRepo.getActivity(id);
        prospectActivity.setCompleteDate(Spirit.getDate());
        prospectActivity.setCompletedByUserId(user.getId());
        prospectRepo.setCompleted(prospectActivity);

        data.put("message", "Successfully completed activity.");
        return "[redirect]/prospects/history/" + prospectActivity.getProspectId();
    }

    public String deleteActivity(Long id, RequestData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        ProspectActivity activity = prospectRepo.getActivity(id);
        prospectRepo.deleteActivity(id);
        data.put("message", "Successfully deleted activity");
        return "[redirect]/prospects/" + activity.getProspectId();
    }


    public String saveEffort(Long id, RequestData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        Prospect prospect = prospectRepo.get(id);
        Effort effort = new Effort();
        effort.setProspectId(prospect.getId());
        effort.setStartDate(Spirit.getDate());
        effort.setStartingStatusId(prospect.getStatusId());
        effortRepo.save(effort);

        data.put("message", "Successfully started effort. All sales activities will be recorded and a clear view at your sales funnels will be visible.");
        return "[redirect]/prospects/" + id;
    }

    public String stopEffort(Long id, RequestData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        Prospect prospect = prospectRepo.get(id);
        Effort effort = effortRepo.getProspectEffort(id, false);
        effort.setEndDate(Spirit.getDate());
        effort.setEndingStatusId(prospect.getStatusId());
        effort.setFinished(true);
        Status endingStatus = statusRepo.get(prospect.getStatusId());
        if(endingStatus.equals(Spirit.CUSTOMER_STATUS)){
            effort.setSuccess(true);
        }
        effortRepo.update(effort);
        data.put("message", "Successfully stopped effort.");
        return "[redirect]/prospects/" + id;
    }

    public String history(Long id, RequestData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        List<ProspectActivity> prospectActivities = prospectRepo.getActivities();
        prospectActivities.stream().forEach((prospectActivity -> setPretty(prospectActivity)));

        Prospect prospect = prospectRepo.get(id);
        List<ProspectActivity> activities = prospectRepo.getCompletedActivities(id);
        activities.stream().forEach((prospectActivity -> setPretty(prospectActivity)));

        data.put("prospectActivities", prospectActivities);
        data.put("prospect", prospect);
        data.put("activities", activities);
        return "/pages/prospect/history.jsp";
    }

    public String editActivity(Long id, RequestData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        List<ProspectActivity> prospectActivities = prospectRepo.getActivities();
        prospectActivities.stream().forEach((prospectActivity -> setPretty(prospectActivity)));

        ProspectActivity activity = prospectRepo.getActivity(id);
        setPretty(activity);

        data.put("activity", activity);
        data.put("prospectActivities", prospectActivities);
        return "/pages/prospect_activity/edit.jsp";
    }

    public String updateActivity(Long id, RequestData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        ProspectActivity prospectActivity = prospectRepo.getActivity(id);
        String phones = req.getParameter("phones");
        checkCorrectPhonesFormat(phones, prospectActivity);

        Boolean five = getBooleanNotification("five", req);
        Boolean fifteen = getBooleanNotification("fifteen", req);
        Boolean thirty = getBooleanNotification("thirty", req);

        prospectActivity.setCompleted(false);
        prospectActivity.setFiveReminder(five);
        prospectActivity.setFifteenReminder(fifteen);
        prospectActivity.setThirtyReminder(thirty);

        prospectRepo.updateActivity(prospectActivity);

        data.put("message", "Successfully updated sales activity");
        return "[redirect]/prospects/activity/edit/" + id;
    }

    public Boolean getBooleanNotification(String parameter, HttpServletRequest req){
        String on = req.getParameter(parameter);
        if(on != null &&
                on.equals("on")){
            return true;
        }
        return false;
    }


    public String editNotes(Long id, RequestData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        Prospect prospect = prospectRepo.get(id);
        List<ProspectActivity> prospectActivities = prospectRepo.getActivities();
        prospectActivities.stream().forEach((prospectActivity -> setPretty(prospectActivity)));

        data.put("prospect", prospect);
        data.put("prospectActivities", prospectActivities);
        return "/pages/prospect/notes.jsp";
    }

    public String updateNotes(Long id, RequestData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        Prospect prospect = prospectRepo.get(id);
        String notes = req.getParameter("notes");
        prospect.setNotes(notes);
        prospectRepo.update(prospect);

        data.put("message", "Successfully updated notes!");
        return "[redirect]/prospects/notes/edit/" + id;
    }
}
