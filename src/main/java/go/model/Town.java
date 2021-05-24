package go.model;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class Town {
    Long id;
    String name;
    String townUri;
    Long count;
    Long stateId;

    List<Organization> organizations;

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

    public String getTownUri() {
        return townUri;
    }

    public void setTownUri(String townUri) {
        this.townUri = townUri;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }

    public String getCountZero(){
        return NumberFormat.getInstance(Locale.US).format(count);
    }

}
