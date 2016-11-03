package de.odinoxin.aidcloud.plugins.contact.types;

import de.odinoxin.aidcloud.plugins.RecordItem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContactTypeEntity")
public class ContactTypeEntity extends RecordItem {

    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "code")
    private String code;
    @XmlElement(name = "regex")
    private String regex;

    public ContactTypeEntity() {
        super();
    }

    public ContactTypeEntity(int id) {
        super(id);
    }

    public ContactTypeEntity(int id, String name, String code, String regex) {
        this(id);
        this.name = name;
        this.code = code;
        this.regex = regex;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getRegex() {
        return regex;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }
}
