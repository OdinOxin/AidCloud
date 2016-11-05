package de.odinoxin.aidcloud.plugins.contact.information;

import de.odinoxin.aidcloud.plugins.Recordable;
import de.odinoxin.aidcloud.plugins.contact.types.ContactType;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContactInformationEntity")
@Entity
@Table(name = "ContactInformation")
public class ContactInformation implements Recordable {

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

    @Override
    public Object clone() {
        return new ContactInformation(this.getId(), this.getContactType(), this.getInformation());
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public ContactType getContactType() {
        if (Hibernate.isInitialized(contactType))
            return contactType;
        return null;
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
