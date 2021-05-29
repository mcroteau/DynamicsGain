package go.repo;

import eco.m1.Q;
import eco.m1.annotate.Inject;
import eco.m1.annotate.Jdbc;
import go.model.Donation;

import java.util.ArrayList;
import java.util.List;

@Jdbc
public class DonationRepo {

    @Inject
    Q q;

    public long getId() {
        String sql = "select max(id) from donations";
        long id = q.getLong(sql, new Object[]{});
        return id;
    }

    public Long getCount() {
        String sql = "select count(*) from donations";
        Long count = q.getLong(sql, new Object[]{});
        return count;
    }

    public Donation get(long id){
        String sql = "select * from donations where id = {}";
        Donation donation = (Donation) q.get(sql, new Object[] { id }, Donation.class);
        return donation;
    }

    public Donation get(String subscriptionId){
        String sql = "select * from donations where subscription_id = '{}'";
        Donation donation = (Donation) q.get(sql, new Object[] { subscriptionId }, Donation.class);
        return donation;
    }

    public Donation save(Donation donation){
        String sql = "insert into donations " +
                "(amount, charge_id, subscription_id, user_id, organization_id, donation_date, processed) values " +
                "({}, '{}', '{}', {}, {}, {}, {})";
        q.save(sql, new Object[] {
                donation.getAmount(),
                donation.getChargeId(),
                donation.getSubscriptionId(),
                donation.getUserId(),
                donation.getOrganizationId(),
                donation.getDonationDate(),
                donation.getProcessed()
        });
        Long id = getId();
        Donation savedDonation = get(id);
        return savedDonation;
    }

    public List<Donation> getList(){
        String sql = "select * from donations";
        List<Donation> donations = (ArrayList) q.getList(sql, new Object[]{}, Donation.class);
        return donations;
    }

    public List<Donation> getList(long userId) {
        String sql = "select * from donations where user_id = {}";
        List<Donation> donations = (ArrayList) q.getList(sql, new Object[]{ userId }, Donation.class);
        return donations;
    }

    public List<Donation> getListOrganization(long organizationId) {
        String sql = "select * from donations where organization_id = {}";
        List<Donation> donations = (ArrayList) q.getList(sql, new Object[]{ organizationId }, Donation.class);
        return donations;
    }

    public boolean update(Donation donation) {
        System.out.println(
                        donation.getProcessed() + " :: " +
                        donation.getChargeId() + " :: " +
                        donation.getSubscriptionId() + " :: " +
                        donation.isCancelled() + " :: " +
                        donation.getId());

        String sql = "update donations set processed = {}, charge_id = '{}', subscription_id = '{}', cancelled = {} where id = {}";
        q.update(sql, new Object[]{
                donation.getProcessed(),
                donation.getChargeId(),
                donation.getSubscriptionId(),
                donation.isCancelled(),
                donation.getId()
        });
        return true;
    }
}
