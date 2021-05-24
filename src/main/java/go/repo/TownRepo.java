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
        String sql = "select * from towns order by id desc limit 1";
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

    public Town get(String uri) {
        String sql = "select * from towns where town_uri = ?";
        Town town = (Town) q.get(sql, new Object[] { uri }, Town.class);
        return town;
    }

    public List<Town> getList(){
        String sql = "select * from towns";
        List<Town> towns = (ArrayList) q.getList(sql, new Object[]{}, Town.class);
        return towns;
    }

    public List<Town> getList(Long id){
        String sql = "select * from towns where state_id = {}";
        List<Town> towns = new ArrayList<>();
        try {
            towns =(ArrayList) q.getList(sql, new Object[]{id}, Town.class);
        }catch(Exception ex){}
        return towns;
    }

    public Town save(Town town){
        String sql = "insert into towns (name, count, state_id) values ('{}', {}, {})";
        q.save(sql, new Object[] {
                town.getName(),
                town.getCount(),
                town.getStateId()
        });

        Town savedTown = getLastInserted();
        return savedTown;
    }

    public boolean update(Town town) {
        String sql = "update towns set name = '{}', count = {}, town_uri = '{}', state_id = {} where id = {}";
        q.update(sql, new Object[] {
                town.getName(),
                town.getCount(),
                town.getTownUri(),
                town.getStateId(),
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
