package go.model;

public class DynamicsProduct {

    private static String TYPE = "service";

    Long id;
    String stripeId;
    String nickname;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStripeId() {
        return stripeId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setStripeId(String stripeId) {
        this.stripeId = stripeId;
    }

    public String getStripeType() {
        return DynamicsProduct.TYPE;
    }

}
