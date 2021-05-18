package go.repo;

import eco.m1.Q;
import go.model.Activity;
import eco.m1.annotate.Inject;
import eco.m1.annotate.Jdbc;

import java.util.ArrayList;
import java.util.List;

@Jdbc
public class ActivityRepo {

    @Inject
    Q q;

    public Activity getLastInserted() {
        String sql = "select * from from activities order by id desc limit 1";
        Activity activity = (Activity) q.get(sql, new Object[]{}, Activity.class);
        return activity;
    }

    public Long getCount() {
        String sql = "select count(*) from activities";
        Long count = q.getLong(sql, new Object[]{});
        return count;
    }

    public Activity get(long id){
        String sql = "select * from activities where id = {}";
        Activity activity = (Activity) q.get(sql, new Object[]{ id }, Activity.class);
        return activity;
    }

    public List<Activity> getList(){
        String sql = "select * from activities";
        List<Activity> activities = (ArrayList) q.getList(sql, new Object[]{}, Activity.class);
        return activities;
    }

    public Activity save(Activity activity){
        String sql = "insert into activities (name) values ('{}')";
        q.update(sql, new Object[] {
            activity.getName()
        });

        Activity savedActivity = getLastInserted();
        return savedActivity;
    }

    public boolean update(Activity activity) {
        String sql = "update activities set name = {} where id = {}";
        q.update(sql, new Object[] {
                activity.getName(),
                activity.getId()
        });
        return true;
    }

    public boolean delete(long id){
        String sql = "delete from activities where id = {}";
        q.delete(sql, new Object[] { id });
        return true;
    }

}
