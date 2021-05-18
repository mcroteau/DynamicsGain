package go.repo;

import eco.m1.Q;
import go.model.Effort;
import go.model.ProspectActivity;
import eco.m1.annotate.Inject;
import eco.m1.annotate.Jdbc;

import java.util.ArrayList;
import java.util.List;

@Jdbc
public class EffortRepo {

    @Inject
    Q q;

    public Effort getLastInserted() {
        String sql = "select * from efforts order by id desc limit 1";
        Effort effort = (Effort) q.get(sql, new Object[]{}, Effort.class);
        return effort;
    }

    public Long getCount() {
        String sql = "select count(*) from efforts";
        Long count = q.getLong(sql, new Object[]{});
        return count;
    }

    public Long getCount(Long id) {
        String sql = "select count(*) from efforts where status_id = {}";
        Long count = (Long) q.get(sql, new Object[]{ id }, Long.class);
        return count;
    }

    public Effort get(long id){
        String sql = "select * from efforts where id = {}";
        Effort effort = (Effort) q.get(sql, new Object[]{ id }, Effort.class);
        return effort;
    }

    public List<Effort> getList(){
        String sql = "select * from efforts";
        List<Effort> efforts = (ArrayList) q.getList(sql, new Object[]{}, Effort.class);
        return efforts;
    }

    public List<Effort> getList(Long id){
        String sql = "select * from efforts where prospect_id = {}";
        List<Effort> efforts = (ArrayList) q.getList(sql, new Object[]{ id }, Effort.class);
        return efforts;
    }

    public Effort save(Effort effort){
        String sql = "insert into efforts (start_date, prospect_id, starting_status_id) values ({}, {}, {})";
        q.save(sql, new Object[] {
                effort.getStartDate(),
                effort.getProspectId(),
                effort.getStartingStatusId()
        });

        Effort savedEffort = getLastInserted();
        return savedEffort;
    }

    public boolean update(Effort effort) {
        String sql = "update efforts set end_date = {}, finished = {}, ending_status_id = {}, success = {} where id = {}";
        q.update(sql, new Object[] {
                effort.getEndDate(),
                effort.getFinished(),
                effort.getEndingStatusId(),
                effort.getSuccess(),
                effort.getId()
        });
        return true;
    }

    public boolean delete(long id){
        String sql = "delete from efforts where id = {}";
        q.delete(sql, new Object[] { id });
        return true;
    }

    public List<ProspectActivity> getActivities(Long id){
        String sql = "select pa.id, pa.activity_id, pa.effort_id, pa.complete_date, a.name " +
                "from prospect_activities pa inner join activities a on pa.activity_id = a.id " +
                "where pa.effort_id = {} order by pa.complete_date asc";

        List<ProspectActivity> prospectActivities = (ArrayList) q.getList(sql, new Object[]{ id }, ProspectActivity.class);
        return prospectActivities;
    }

    public boolean deleteActivity(Long id) {
        String sql = "delete from prospect_activities where effort_id = {}";
        q.delete(sql, new Object[] { id });
        return true;
    }

    public boolean deleteActivities(Long id) {
        String sql = "delete from prospect_activities where effort_id = {}";
        q.delete(sql, new Object[] { id });
        return true;
    }

    public Long getActivityCount() {
        String sql = "select count(*) from prospect_activities where effort_id >= 0";
        Long count = (Long) q.get(sql, new Long[]{}, Long.class);
        return count;
    }

    public List<Effort> getSuccesses() {
        String sql = "select * from efforts where success = true";
        List<Effort> efforts = (ArrayList) q.get(sql, new Object[]{ }, Effort.class);
        return efforts;
    }

    public Effort getProspectEffort(Long id, Boolean finished) {
        String sql = "select * from efforts where prospect_id = {} and finished = {}";
        Effort effort = (Effort) q.get(sql, new Object[]{ id, finished }, Effort.class);
        return effort;
    }
}
