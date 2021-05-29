package go.model;


public class User {

	long id;
	String uuid;
	String username;
	String password;
	String cleanPassword;
	long dateCreated;
	String phone;

	boolean charity;
	String stripeCustomerId;
	Long organizationId;
	String stripeAccountId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCleanPassword() {
		return cleanPassword;
	}

	public void setCleanPassword(String cleanPassword) {
		this.cleanPassword = cleanPassword;
	}

	public long getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(long dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isCharity() {
		return charity;
	}

	public void setCharity(boolean charity) {
		this.charity = charity;
	}

	public String getStripeCustomerId() {
		return stripeCustomerId;
	}

	public void setStripeCustomerId(String stripeCustomerId) {
		this.stripeCustomerId = stripeCustomerId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getStripeAccountId() {
		return stripeAccountId;
	}

	public void setStripeAccountId(String stripeAccountId) {
		this.stripeAccountId = stripeAccountId;
	}
}

