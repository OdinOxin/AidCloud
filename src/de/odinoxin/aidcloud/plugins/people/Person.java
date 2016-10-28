package de.odinoxin.aidcloud.plugins.people;

import de.odinoxin.aidcloud.plugins.addresses.Address;
import de.odinoxin.aidcloud.plugins.contact.information.ContactInformation;
import de.odinoxin.aidcloud.plugins.languages.Language;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonEntity")
@Entity
@Table(name = "Person")
public class Person {

    @Id
    @GeneratedValue
    @XmlElement(name = "id")
    private int id;
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "forename")
    private String forename;
    @XmlElement(name = "code")
    private String code;
    @XmlElement(name = "pwd")
    private String pwd;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @XmlElement(name = "language")
    private Language language;
    @XmlElement(name = "address")
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Address address;
    @XmlElement(name = "contactInformation")
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<ContactInformation> contactInformation = new ArrayList<>();

    public Person() {

    }

    public Person(int id) {
        this();
        this.id = id;
    }

    public Person(int id, String name, String forename, String code, Language language, Address address, List<ContactInformation> contactInformation) {
        this(id);
        this.name = name;
        this.forename = forename;
        this.code = code;
        this.language = language;
        this.address = address;
        this.contactInformation = contactInformation;
    }

    public int getId() {
        return id;
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

    public Language getLanguage() {
        return language;
    }

    public Address getAddress() {
        return address;
    }

    public List<ContactInformation> getContactInformation() {
        return contactInformation;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setLanguage(Language language) {
        this.language = language;
    }

    public void setAddressId(Address address) {
        this.address = address;
    }

    public void setContactInformation(List<ContactInformation> contactInformation) {
        this.contactInformation = contactInformation;
    }
}
