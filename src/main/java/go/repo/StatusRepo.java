package go.repo;

import eco.m1.Q;
import go.model.Status;
import eco.m1.annotate.Inject;
import eco.m1.annotate.Jdbc;

import java.util.ArrayList;
import java.util.List;

@Jdbc
public class StatusRepo {

    @Inject
    Q q;

    public Status getLastInserted() {
        String sql = "select * from from statuses order by id desc limit 1";
        Status status = (Status) q.get(sql, new Object[]{}, Status.class);
        return status;
    }

    public long getCount() {
        String sql = "select count(*) from statuses";
        Long count = q.getLong(sql, new Object[] { });
        return count;
    }

    public Status get(long id){
        String sql = "select * from statuses where id = {}";
        Status status = (Status) q.get(sql, new Object[]{ id }, Status.class);
        return status;
    }

    public List<Status> getList(){
        String sql = "select * from statuses";
        List<Status> statuses = (ArrayList) q.getList(sql, new Object[]{}, Status.class);
        return statuses;
    }

    public Status save(Status status){
        String sql = "insert into statuses (name) values ('{}')";
        q.update(sql, new Object[] {
                status.getName()
        });

        Status savedStatus = getLastInserted();
        return savedStatus;
    }

    public boolean update(Status status) {
        String sql = "update statuses set name = {} where id = {}";
        q.update(sql, new Object[] {
                status.getName(),
                status.getId()
        });
        return true;
    }

    public boolean delete(long id){
        String sql = "delete from statuses where id = {}";
        q.delete(sql, new Object[] { id });
        return true;
    }

}
