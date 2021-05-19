package go.repo;

import eco.m1.Q;
import eco.m1.annotate.Inject;
import eco.m1.annotate.Jdbc;
import go.model.Nation;

import java.util.ArrayList;
import java.util.List;

@Jdbc
public class NationRepo {

    @Inject
    Q q;

    public Nation getLastInserted() {
        String sql = "select * from from nations order by id desc limit 1";
        Nation nation = (Nation) q.get(sql, new Object[]{}, Nation.class);
        return nation;
    }

    public long getCount() {
        String sql = "select count(*) from nations";
        Long count = q.getLong(sql, new Object[] { });
        return count;
    }

    public Nation get(long id){
        String sql = "select * from nations where id = {}";
        Nation nation = (Nation) q.get(sql, new Object[]{ id }, Nation.class);
        return nation;
    }

    public List<Nation> getList(){
        String sql = "select * from nations";
        List<Nation> nations = (ArrayList) q.getList(sql, new Object[]{}, Nation.class);
        return nations;
    }

    public Nation save(Nation nation){
        String sql = "insert into nations (name) values ('{}')";
        q.update(sql, new Object[] {
                nation.getName()
        });

        Nation savedNation = getLastInserted();
        return savedNation;
    }

    public boolean update(Nation nation) {
        String sql = "update nations set name = {} where id = {}";
        q.update(sql, new Object[] {
                nation.getName(),
                nation.getId()
        });
        return true;
    }

    public boolean delete(long id){
        String sql = "delete from nations where id = {}";
        q.delete(sql, new Object[] { id });
        return true;
    }

}
