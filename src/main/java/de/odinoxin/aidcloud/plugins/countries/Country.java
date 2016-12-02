package de.odinoxin.aidcloud.plugins.countries;

import de.odinoxin.aidcloud.plugins.Recordable;

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
public class Country implements Recordable {

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

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null
                || obj.getClass() != this.getClass())
            return false;
        Country country = (Country) obj;
        return country.getId() == this.getId()
                && ((country.getAlpha2() == null && this.getAlpha2() == null) || (country.getAlpha2() != null && country.getAlpha2().equals(this.getAlpha2())))
                && ((country.getAlpha3() == null && this.getAlpha3() == null) || (country.getAlpha3() != null && country.getAlpha3().equals(this.getAlpha3())))
                && ((country.getName() == null && this.getName() == null) || (country.getName() != null && country.getName().equals(this.getName())))
                && ((country.getAreaCode() == null && this.getAreaCode() == null) || (country.getAreaCode() != null && country.getAreaCode().equals(this.getAreaCode())));
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getAlpha2() {
        return alpha2;
    }

    public void setAlpha2(String alpha2) {
        this.alpha2 = alpha2;
    }

    public String getAlpha3() {
        return alpha3;
    }

    public void setAlpha3(String alpha3) {
        this.alpha3 = alpha3;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
}