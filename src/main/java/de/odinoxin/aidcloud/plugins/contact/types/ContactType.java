package de.odinoxin.aidcloud.plugins.contact.types;

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
@XmlType(name = "ContactTypeEntity")
@Entity
@Table(name = "ContactType")
public class ContactType implements Recordable {

    @Id
    @GeneratedValue
    @XmlElement(name = "id")
    private int id;
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "code")
    private String code;
    @XmlElement(name = "regex")
    private String regex;

    public ContactType() {

    }

    public ContactType(int id) {
        this();
        this.id = id;
    }

    public ContactType(int id, String name, String code, String regex) {
        this(id);
        this.name = name;
        this.code = code;
        this.regex = regex;
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
        ContactType contactType = (ContactType) obj;
        return contactType.getId() == this.getId()
                && ((contactType.getName() == null && this.getName() == null) || (contactType.getName() != null && contactType.getName().equals(this.getName())))
                && ((contactType.getCode() == null && this.getCode() == null) || (contactType.getCode() != null && contactType.getCode().equals(this.getCode())))
                && ((contactType.getRegex() == null && this.getRegex() == null) || (contactType.getRegex() != null && contactType.getRegex().equals(this.getRegex())));
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }
}
