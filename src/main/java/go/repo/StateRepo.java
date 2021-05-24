package go.repo;

import eco.m1.Q;
import eco.m1.annotate.Inject;
import eco.m1.annotate.Jdbc;
import go.model.State;

import java.util.ArrayList;
import java.util.List;

@Jdbc
public class StateRepo {

    @Inject
    Q q;

    public State getLastInserted() {
        String sql = "select * from states order by id desc limit 1";
        State state = (State) q.get(sql, new Object[]{}, State.class);
        return state;
    }

    public long getCount() {
        String sql = "select count(*) from states";
        Long count = q.getLong(sql, new Object[] { });
        return count;
    }

    public State get(long id){
        String sql = "select * from states where id = {}";
        State state = (State) q.get(sql, new Object[]{ id }, State.class);
        return state;
    }

    public State get(String name){
        String sql = "select * from states where lower(name) = '{}'";
        State state = (State) q.get(sql, new Object[]{ name.toLowerCase() }, State.class);
        return state;
    }

    public List<State> getList(){
        String sql = "select * from states";
        List<State> states = (ArrayList) q.getList(sql, new Object[]{}, State.class);
        return states;
    }

    public State save(State state){
        String sql = "insert into states (name, abbreviation, nation_id) values ('{}', '{}', {})";
        q.save(sql, new Object[] {
                state.getName(),
                state.getAbbreviation(),
                state.getNationId()
        });

        State savedState = getLastInserted();
        return savedState;
    }

    public boolean update(State state) {
        String sql = "update states set name = '{}', abbreviation = '{}', nation_id = {} where id = {}";
        q.update(sql, new Object[] {
                state.getName(),
                state.getAbbreviation(),
                state.getNationId(),
                state.getId()
        });
        return true;
    }

    public boolean delete(long id){
        String sql = "delete from states where id = {}";
        q.delete(sql, new Object[] { id });
        return true;
    }

}
