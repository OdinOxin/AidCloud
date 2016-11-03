package de.odinoxin.aidcloud.plugins.people;

import de.odinoxin.aidcloud.plugins.RecordItem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonEntity")
public class PersonEntity extends RecordItem {

    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "forename")
    private String forename;
    @XmlElement(name = "code")
    private String code;
    @XmlElement(name = "pwd")
    private String pwd;
    @XmlElement(name = "language")
    private int language;
    @XmlElement(name = "addressId")
    private int addressId;

    public PersonEntity() {
        super();
    }

    public PersonEntity(int id) {
        this();
        this.id = id;
    }

    public PersonEntity(int id, String name, String forename, String code, int language, int addressId) {
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

    public int getLanguage() {
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

    public void setLanguage(int language) {
        this.language = language;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
}
