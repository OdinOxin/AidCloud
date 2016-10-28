package de.odinoxin.aidcloud.plugins.countries;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CountryEntity")
@Entity
@Table(name = "Country")
public class Country {

    @Id
    @GeneratedValue
    @XmlElement(name = "id")
    private int id;
    @XmlElement(name = "alpha2")
    private String alpha2;
    @XmlElement(name = "alpha3")
    private String alpha3;
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "areaCode")
    private String areaCode;

    public Country() {

    }

    public Country(int id) {
        this();
        this.id = id;
    }

    public Country(int id, String alpha2, String alpha3, String name, String areaCode) {
        this(id);
        this.alpha2 = alpha2;
        this.alpha3 = alpha3;
        this.name = name;
        this.areaCode = areaCode;
    }

    public int getId() {
        return id;
    }

    public String getAlpha2() {
        return alpha2;
    }

    public String getAlpha3() {
        return alpha3;
    }

    public String getName() {
        return name;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAlpha2(String alpha2) {
        this.alpha2 = alpha2;
    }

    public void setAlpha3(String alpha3) {
        this.alpha3 = alpha3;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
}