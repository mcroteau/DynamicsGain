package go.model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class Charge {
    Long id;
    String stripeId;
    BigDecimal amount;
    Organization organization;
    String donationDate;

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

    public BigDecimal getAmount() {
        return amount;
    }

    public String getAmountZero(){
        return NumberFormat.getCurrencyInstance(Locale.US).format(amount);
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getDonationDate() {
        return donationDate;
    }

    public void setDonationDate(String donationDate) {
        this.donationDate = donationDate;
    }
}
