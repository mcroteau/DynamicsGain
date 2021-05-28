package go.model;

public class OwnershipRequest {
    Long id;
    String name;
    String email;
    String phone;
    Long dateRequested;
    Boolean approved;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getDateRequested() {
        return dateRequested;
    }

    public void setDateRequested(Long dateRequested) {
        this.dateRequested = dateRequested;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }
}
