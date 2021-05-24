package go.model;

import java.text.NumberFormat;
import java.util.Locale;

public class State {
    Long id;
    String name;
    String abbreviation;
    Long nationId;
    Long count;
    String countZero;

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

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public Long getNationId() {
        return nationId;
    }

    public void setNationId(Long nationId) {
        this.nationId = nationId;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public void setCountZero(String countZero){
        this.countZero = countZero;
    }

    public String getCountZero(){
        return countZero;
    }

}
