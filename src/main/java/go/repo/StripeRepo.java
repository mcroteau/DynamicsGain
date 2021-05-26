package go.repo;

import eco.m1.Q;
import eco.m1.annotate.Inject;
import eco.m1.annotate.Jdbc;
import go.model.DynamicsPrice;
import go.model.DynamicsProduct;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Jdbc
public class StripeRepo {

    @Inject
    Q q;

    public long getId() {
        String sql = "select max(id) from prices";
        long id = q.getLong(sql, new Object[]{});
        return id;
    }

    public long getProductId() {
        String sql = "select max(id) from products";
        long id = q.getLong(sql, new Object[]{});
        return id;
    }

    public long getPriceId() {
        String sql = "select max(id) from prices";
        long id = q.getLong(sql, new Object[]{});
        return id;
    }


    public Integer getCount() {
        String sql = "select count(*) from prices";
        Integer count = q.getInteger(sql, new Object[]{});
        return count;
    }

    public DynamicsProduct getProduct(long id){
        String sql = "select * from products where id = {}";
        DynamicsProduct dynamicsProduct = (DynamicsProduct) q.get(sql, new Object[] { id }, DynamicsProduct.class);
        return dynamicsProduct;
    }

    public DynamicsPrice getPrice(long id){
        String sql = "select * from prices where id = {}";
        DynamicsPrice dynamicsPrice = (DynamicsPrice) q.get(sql, new Object[] { id }, DynamicsPrice.class);
        return dynamicsPrice;
    }

    public DynamicsPrice getPriceAmount(BigDecimal amount){
        try {
            String sql = "select * from prices where amount = {}";
            DynamicsPrice dynamicsPrice = (DynamicsPrice) q.get(sql, new Object[]{amount}, DynamicsPrice.class);
            return dynamicsPrice;
        }catch(Exception e){}
        return null;
    }

    public List<DynamicsPrice> getList(){
        String sql = "select * from prices";
        List<DynamicsPrice> dynamicsPrices = (ArrayList) q.getList(sql, new Object[]{}, DynamicsPrice.class);
        return dynamicsPrices;
    }

    public DynamicsProduct saveProduct(DynamicsProduct dynamicsProduct){
        String sql = "insert into products (nickname, stripe_id) values ('{}', '{}')";
        q.save(sql, new Object[] {
                dynamicsProduct.getNickname(), dynamicsProduct.getStripeId()
        });
        Long id = getProductId();
        DynamicsProduct savedProduct = getProduct(id);
        return savedProduct;
    }

    public DynamicsPrice savePrice(DynamicsPrice dynamicsPrice){
        String sql = "insert into prices (amount, nickname, product_id, stripe_id) values ({}, '{}', {}, '{}')";
        q.save(sql, new Object[] {
                dynamicsPrice.getAmount(), dynamicsPrice.getNickname(), dynamicsPrice.getProductId(), dynamicsPrice.getStripeId()
        });
        Long id = getPriceId();
        DynamicsPrice savedPrice = getPrice(id);
        return savedPrice;
    }

    public boolean deleteProduct(long id){
        String sql = "delete from products where id = {}";
        q.delete(sql, new Object[] { id });
        return true;
    }

    public boolean deletePrice(long id){
        String sql = "delete from prices where id = {}";
        q.delete(sql, new Object[] { id });
        return true;
    }

    public DynamicsPrice getPriceProductId(Long id) {
        try{
            String sql = "select * from prices where product_id = {}";
            DynamicsPrice dynamicsPrice = (DynamicsPrice) q.get(sql, new Object[] { id }, DynamicsPrice.class);
            return dynamicsPrice;
        }catch(Exception ex){ }
        return null;
    }


    public boolean updatePrice(DynamicsPrice dynamicsPrice) {
        String sql = "update prices set stripe_id = '{}' where id = {}";
        q.update(sql, new Object[] {
                dynamicsPrice.getStripeId(), dynamicsPrice.getId()
        });
        return true;
    }


    public boolean updateProduct(DynamicsProduct dynamicsProduct) {
        String sql = "update products set stripe_id = '{}' where id = {}";
        q.update(sql, new Object[] {
                dynamicsProduct.getStripeId(), dynamicsProduct.getId()
        });
        return true;
    }

    public DynamicsProduct getProductStripeId(String stripeId) {
        try {
            String sql = "select * from products where stripe_id = '{}'";
            DynamicsProduct dynamicsProduct = (DynamicsProduct) q.get(sql, new Object[]{stripeId}, DynamicsProduct.class);
            return dynamicsProduct;
        }catch(Exception ex){ }
        return null;
    }

}
