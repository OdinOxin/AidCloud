package de.odinoxin.aidcloud.plugins.contact.information;

import de.odinoxin.aidcloud.plugins.RecordItem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContactInformationEntity")
public class ContactInformationEntity extends RecordItem {

    @XmlElement(name = "contactType")
    private int contactType;
    @XmlElement(name = "information")
    private String information;

    public ContactInformationEntity() {
        super();
    }

    public ContactInformationEntity(int id) {
        super(id);
    }

    public ContactInformationEntity(int id, int contactType, String information) {
        this(id);
        this.contactType = contactType;
        this.information = information;
    }

    public int getContactType() {
        return contactType;
    }

    public String getInformation() {
        return information;
    }

    public void setContactType(int contactType) {
        this.contactType = contactType;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}
