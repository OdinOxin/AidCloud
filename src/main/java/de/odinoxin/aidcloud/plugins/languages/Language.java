package de.odinoxin.aidcloud.plugins.languages;

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
@XmlType(name = "LanguageEntity")
@Entity
@Table(name = "Language")
public class Language implements Recordable {

    @Id
    @GeneratedValue
    @XmlElement(name = "id")
    private int id;
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "code")
    private String code;

    public Language() {

    }

    public Language(int id) {
        this();
        this.id = id;
    }

    public Language(int id, String name, String code) {
        this(id);
        this.name = name;
        this.code = code;
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
        Language language = (Language) obj;
        return language.getId() == this.getId()
                && ((language.getName() == null && this.getName() == null) || (language.getName() != null && language.getName().equals(this.getName())))
                && ((language.getCode() == null && this.getCode() == null) || (language.getCode() != null && language.getCode().equals(this.getCode())));
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
}
