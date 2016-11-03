package de.odinoxin.aidcloud.plugins.addresses;

import de.odinoxin.aidcloud.plugins.RecordItem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AddressEntity")
public class AddressEntity extends RecordItem {

    @XmlElement(name = "street")
    private String street;
    @XmlElement(name = "hsNo")
    private String hsNo;
    @XmlElement(name = "zip")
    private String zip;
    @XmlElement(name = "city")
    private String city;
    @XmlElement(name = "country")
    private int country;

    public AddressEntity() {
        super();
    }

    public AddressEntity(int id) {
        super(id);
    }

    public AddressEntity(int id, String street, String hsNo, String zip, String city, int country) {
        this(id);
        this.street = street;
        this.hsNo = hsNo;
        this.zip = zip;
        this.city = city;
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public String getHsNo() {
        return hsNo;
    }

    public String getZip() {
        return zip;
    }

    public String getCity() {
        return city;
    }

    public int getCountry() {
        return country;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setHsNo(String hsNo) {
        this.hsNo = hsNo;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(int country) {
        this.country = country;
    }
}
