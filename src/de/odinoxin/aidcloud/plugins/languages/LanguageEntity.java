package de.odinoxin.aidcloud.plugins.languages;

import de.odinoxin.aidcloud.plugins.RecordItem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LanguageEntity")
public class LanguageEntity extends RecordItem {

    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "code")
    private String code;

    public LanguageEntity() {
        super();
    }

    public LanguageEntity(int id) {
        super(id);
    }

    public LanguageEntity(int id, String name, String code) {
        this(id);
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
