package go.repo;

import eco.m1.Q;
import eco.m1.annotate.Inject;
import eco.m1.annotate.Jdbc;
import go.model.Organization;

import java.util.ArrayList;
import java.util.List;

@Jdbc
public class OrganizationRepo {

    @Inject
    Q q;

    public Organization getLastInserted() {
        String sql = "select * from organizations order by id desc limit 1";
        Organization organization = (Organization) q.get(sql, new Object[]{}, Organization.class);
        return organization;
    }

    public long getCount() {
        String sql = "select count(*) from organizations";
        Long count = q.getLong(sql, new Object[] { });
        return count;
    }

    public Organization get(long id){
        String sql = "select * from organizations where id = {}";
        Organization organization = (Organization) q.get(sql, new Object[]{ id }, Organization.class);
        return organization;
    }

    public Organization get(String uri) {
        String sql = "select * from organizations where uri = '{}'";
        Organization location = (Organization) q.get(sql, new Object[] { uri }, Organization.class);
        return location;
    }

    public List<Organization> getList(){
        String sql = "select * from organizations";
        List<Organization> organizations = (ArrayList) q.getList(sql, new Object[]{}, Organization.class);
        return organizations;
    }

    public List<Organization> getList(long id) {
        String sql = "select * from organizations where town_id = ?";
        List<Organization> organizations = (List) q.getList(sql, new Object[]{ id }, Organization.class);
        return organizations;
    }

    public Organization save(Organization organization){
        String sql = "insert into organizations (name, uri, town_id) values ('{}', '{}', {})";
        q.update(sql, new Object[] {
                organization.getName(),
                organization.getUri(),
                organization.getTownId()
        });

        Organization savedOrganization = getLastInserted();
        return savedOrganization;
    }

    public boolean update(Organization organization) {
        String sql = "update organizations set name = '{}', description= '{}', uri = '{}', latitude = '{}', longitude = '{}', town_id = {} where id = {}";
        q.update(sql, new Object[] {
                organization.getName(),
                organization.getDescription(),
                organization.getUri(),
                organization.getLatitude(),
                organization.getLongitude(),
                organization.getTownId(),
                organization.getId()
        });
        return true;
    }

    public boolean delete(long id){
        String sql = "delete from organizations where id = {}";
        q.delete(sql, new Object[] { id });
        return true;
    }

    public boolean deleteOrganizations(Long id) {
        String sql = "delete from organizations where town_id = ?";
        q.delete(sql, new Object[] { id });
        return true;
    }
}
