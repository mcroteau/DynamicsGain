package go.repo;

import eco.m1.Q;
import eco.m1.annotate.Inject;
import eco.m1.annotate.Jdbc;
import go.model.Town;

import java.util.ArrayList;
import java.util.List;

@Jdbc
public class TownRepo {

    @Inject
    Q q;

    public Town getLastInserted() {
        String sql = "select * from from towns order by id desc limit 1";
        Town town = (Town) q.get(sql, new Object[]{}, Town.class);
        return town;
    }

    public long getCount() {
        String sql = "select count(*) from towns";
        Long count = q.getLong(sql, new Object[] { });
        return count;
    }

    public Town get(long id){
        String sql = "select * from towns where id = {}";
        Town town = (Town) q.get(sql, new Object[]{ id }, Town.class);
        return town;
    }

    public List<Town> getList(){
        String sql = "select * from towns";
        List<Town> towns = (ArrayList) q.getList(sql, new Object[]{}, Town.class);
        return towns;
    }

    public Town save(Town town){
        String sql = "insert into towns (name) values ('{}')";
        q.update(sql, new Object[] {
                town.getName()
        });

        Town savedTown = getLastInserted();
        return savedTown;
    }

    public boolean update(Town town) {
        String sql = "update towns set name = {} where id = {}";
        q.update(sql, new Object[] {
                town.getName(),
                town.getId()
        });
        return true;
    }

    public boolean delete(long id){
        String sql = "delete from towns where id = {}";
        q.delete(sql, new Object[] { id });
        return true;
    }

}
