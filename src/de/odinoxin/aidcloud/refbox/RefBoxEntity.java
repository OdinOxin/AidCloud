package de.odinoxin.aidcloud.refbox;

import de.odinoxin.aidcloud.plugins.RecordItem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RefBoxEntity")
public class RefBoxEntity extends RecordItem {

    @XmlElement(name = "text")
    private String text;
    @XmlElement(name = "subText")
    private String subText;

    public RefBoxEntity() {
        super();
    }

    public RefBoxEntity(int id) {
        super(id);
    }

    public RefBoxEntity(int id, String text, String subText) {
        this(id);
        this.text = text;
        this.subText = subText;
    }

    public String getText() {
        return text;
    }

    public String getSubText() {
        return subText;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSubText(String subText) {
        this.subText = subText;
    }
}
