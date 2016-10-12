package de.odinoxin.aidcloud.plugins.people;

import de.odinoxin.aidcloud.plugins.RecordItem;

public class Person extends RecordItem {

    private String name;
    private String forename;
    private String code;
    private String pwd;
    private String language;
    private Integer addressId;

    public Person() {
        super();
    }

    public Person(int id) {
        this();
        this.id = id;
    }

    public Person(int id, String name, String forename, String code, String language, int addressId) {
        this(id);
        this.name = name;
        this.forename = forename;
        this.code = code;
        this.language = language;
        this.addressId = addressId;
    }

    public String getName() {
        return name;
    }

    public String getForename() {
        return forename;
    }

    public String getCode() {
        return code;
    }

    public String getPwd() {
        return pwd;
    }

    public String getLanguage() {
        return language;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
}
