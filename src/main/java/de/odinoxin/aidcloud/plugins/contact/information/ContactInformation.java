package de.odinoxin.aidcloud.plugins.contact.information;

import de.odinoxin.aidcloud.plugins.contact.types.ContactType;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContactInformationEntity")
@Entity
@Table(name = "ContactInformation")
public class ContactInformation {

    @Id
    @GeneratedValue
    @XmlElement(name = "id")
    private int id;
    @XmlElement(name = "contactType")
    @OneToOne
    private ContactType contactType;
    @XmlElement(name = "information")
    private String information;

    public ContactInformation() {

    }

    public ContactInformation(int id) {
        this();
        this.id = id;
    }

    public ContactInformation(int id, ContactType contactType, String information) {
        this(id);
        this.contactType = contactType;
        this.information = information;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ContactType getContactType() {
        return contactType;
    }

    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}
