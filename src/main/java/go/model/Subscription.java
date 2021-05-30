package go.model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class Subscription {

    Long id;
    String stripeId;
    BigDecimal amount;
    Organization organization;
    boolean cancelled;
    String donationDate;
    String email;

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

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getDonationDate() {
        return donationDate;
    }

    public void setDonationDate(String donationDate) {
        this.donationDate = donationDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}






