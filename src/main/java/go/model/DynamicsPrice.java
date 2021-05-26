package go.model;

import java.math.BigDecimal;

public class DynamicsPrice {

    private static final String CURRENCY = "usd";
    private static final String FREQUENCY = "month";

    Long id;
    String stripeId;
    Long productId;

    BigDecimal amount;
    String nickname;

    int projectLimit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStripeId() {
        return stripeId;
    }

    public void setStripeId(String stripeId) {
        this.stripeId = stripeId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFrequency() {
        return DynamicsPrice.FREQUENCY;
    }

    public String getCurrency() {
        return DynamicsPrice.CURRENCY;
    }

    public int getProjectLimit() {
        return projectLimit;
    }

    public void setProjectLimit(int projectLimit) {
        this.projectLimit = projectLimit;
    }
}
