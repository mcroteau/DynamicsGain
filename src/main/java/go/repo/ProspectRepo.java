package go.repo;


import go.model.Prospect;
import go.model.ProspectActivity;
import eco.m1.Q;
import eco.m1.annotate.Inject;
import eco.m1.annotate.Jdbc;

import java.util.ArrayList;
import java.util.List;

@Jdbc
public class ProspectRepo {

    @Inject
    Q q;

    public Prospect getLastInserted() {
        String sql = "select * from prospects order by id desc limit 1";
        Prospect prospect = (Prospect) q.get(sql, new Object[]{}, Prospect.class);
        return prospect;
    }
    public ProspectActivity getLastInsertedActivity() {
        String sql = "select * from prospect_activities order by id desc limit 1";
        ProspectActivity prospectActivity = (ProspectActivity) q.get(sql, new Object[]{}, ProspectActivity.class);
        return prospectActivity;
    }
    public Long getCount() {
        String sql = "select count(*) from prospects";
        Long count = q.getLong(sql, new Object[]{});
        return count;
    }

    public Long getCount(Long id) {
        String sql = "select count(*) from prospects where status_id = {}";
        Long count = (Long) q.get(sql, new Object[]{ id }, Long.class);
        return count;
    }

    public Prospect get(long id){
        String sql = "select * from prospects where id = {}";
        Prospect prospect = (Prospect) q.get(sql, new Object[]{ id }, Prospect.class);
        return prospect;
    }

    public List<Prospect> getList(){
        String sql = "select * from prospects";
        List<Prospect> prospects = (ArrayList) q.getList(sql, new Object[]{}, Prospect.class);
        return prospects;
    }

    public List<Prospect> getList(Long id){
        String sql = "select * from prospects where status_id = {}";
        List<Prospect> prospects = (ArrayList) q.getList(sql, new Object[]{id}, Prospect.class);
        return prospects;
    }

    public List<Prospect> getResults(String query) {
        String sql = "select * from prospects where upper(name) like upper('%{}%')";

        List<Prospect> prospects = (ArrayList) q.getList(sql, new Object[] { query }, Prospect.class);
        return prospects;
    }

    public Prospect save(Prospect prospect){
        String sql = "insert into prospects (name, phone, notes, status_id) values ('{}', '{}', '{}', {})";
        q.save(sql, new Object[] {
                prospect.getName(),
                prospect.getPhone(),
                prospect.getNotes(),
                prospect.getStatusId()
        });

        Prospect savedProspect = getLastInserted();
        return savedProspect;
    }

    public boolean update(Prospect prospect) {
        String sql = "update prospects set name = '{}', phone = '{}', email = '{}', status_id = {}, contacts = '{}', notes = '{}' where id = {}";
        q.update(sql, new Object[] {
                prospect.getName(),
                prospect.getPhone(),
                prospect.getEmail(),
                prospect.getStatusId(),
                prospect.getContacts(),
                prospect.getNotes(),
                prospect.getId()
        });
        return true;
    }

    public boolean delete(long id){
        String sql = "delete from prospects where id = {}";
        q.delete(sql, new Object[] { id });
        return true;
    }

    public List<ProspectActivity> getActivities(){
        String sql = "select pa.id, pa.task_date, pa.prospect_id, a.name from " +
                "prospect_activities pa " +
                "inner join activities a on pa.activity_id = a.id " +
                "inner join prospects p on pa.prospect_id = p.id " +
                "where completed = false";
        List<ProspectActivity> prospectActivities = (ArrayList) q.getList(sql, new Object[]{ }, ProspectActivity.class);
        return prospectActivities;
    }

    public ProspectActivity getActivity(long id){
        String sql = "select pa.id, pa.task_date, pa.prospect_id, " +
                    "pa.five_reminder, pa.fifteen_reminder, pa.thirty_reminder, " +
                    "pa.phones, a.name from " +
                    "prospect_activities pa " +
                    "inner join activities a on pa.activity_id = a.id " +
                    "inner join prospects p on pa.prospect_id = p.id " +
                    "where pa.id = {}";
        ProspectActivity prospectActivity = (ProspectActivity) q.get(sql, new Object[]{ id }, ProspectActivity.class);
        return prospectActivity;
    }

    public boolean saveActivity(ProspectActivity prospectActivity) {
        String sql = "insert into prospect_activities (activity_id, prospect_id, task_date, time_zone, phones, effort_id, five_reminder) values ({}, {}, {}, '{}', '{}', {}, {})";
        q.save(sql, new Object[] {
                prospectActivity.getActivityId(),
                prospectActivity.getProspectId(),
                prospectActivity.getTaskDate(),
                prospectActivity.getTimeZone(),
                prospectActivity.getPhones(),
                prospectActivity.getEffortId(),
                prospectActivity.getFiveReminder()
        });
        return true;
    }

    public boolean setCompleted(ProspectActivity prospectActivity){
        String sql = "update prospect_activities set completed = true, complete_date = {}, completed_by_user_id = {} where id = {}";
        q.update(sql, new Object[]{
                prospectActivity.getCompleteDate(),
                prospectActivity.getCompletedByUserId(),
                prospectActivity.getId()
        });
        return true;
    }

    public boolean updateActivity(ProspectActivity prospectActivity) {
        String sql = "update prospect_activities set phones = '{}', " +
                "five_reminder = {}, fifteen_reminder = {}, thirty_reminder = {}, " +
                "notified_five = {}, notified_fifteen = {}, notified_thirty = {} " +
                "where id = {}";
        q.update(sql, new Object[] {
                prospectActivity.getPhones(),
                prospectActivity.getFiveReminder(),
                prospectActivity.getFifteenReminder(),
                prospectActivity.getThirtyReminder(),
                prospectActivity.isNotifiedFive(),
                prospectActivity.isNotifiedFifteen(),
                prospectActivity.isNotifiedThirty(),
                prospectActivity.getId()
        });
        return true;
    }

    public List<ProspectActivity> getUncompletedActivities(Long id){
        String sql = "select pa.id, pa.task_date, pa.prospect_id, a.name, " +
                "pa.five_reminder, pa.fifteen_reminder, pa.thirty_reminder " +
                "from prospect_activities pa " +
                "inner join activities a on pa.activity_id = a.id " +
                "inner join prospects p on pa.prospect_id = p.id " +
                "where completed = false and pa.prospect_id = {}";

        List<ProspectActivity> prospectActivities = (ArrayList) q.getList(sql, new Object[]{ id }, ProspectActivity.class);
        return prospectActivities;
    }

    public List<ProspectActivity> getCompletedActivities(Long id) {
        String sql = "select pa.id, pa.task_date, pa.prospect_id, a.name from " +
                "prospect_activities pa " +
                "inner join activities a on pa.activity_id = a.id " +
                "inner join prospects p on pa.prospect_id = p.id " +
                "where pa.completed = true and pa.prospect_id = {}";

        List<ProspectActivity> prospectActivities = (ArrayList) q.getList(sql, new Object[]{ id }, ProspectActivity.class);
        return prospectActivities;
    }

    public List<ProspectActivity> getIncompleteActivities() {
        String sql = "select pa.id, pa.task_date, pa.prospect_id, pa.time_zone, " +
                "pa.five_reminder, pa.fifteen_reminder, pa.thirty_reminder, " +
                "pa.notified_five, pa.notified_fifteen, pa.notified_thirty, " +
                "pa.phones, p.name as prospect_name, a.name from " +
                "prospect_activities pa " +
                "inner join activities a on pa.activity_id = a.id " +
                "inner join prospects p on pa.prospect_id = p.id " +
                "where completed = false";

        List<ProspectActivity> prospectActivities = (ArrayList) q.getList(sql, new Object[]{}, ProspectActivity.class);
        return prospectActivities;
    }

    public boolean deleteActivity(Long id) {
        String sql = "delete from prospect_activities where id = {}";
        q.delete(sql, new Object[] { id });
        return true;
    }

    public boolean deleteActivities(Long id) {
        String sql = "delete from prospect_activities where prospect_id = {}";
        q.delete(sql, new Object[] { id });
        return true;
    }

    public Long getActivityCount() {
        String sql = "select count(*) from prospect_activities";
        Long count = q.getLong(sql, new Object[]{});
        return count;
    }

}
