package de.odinoxin.aidcloud;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceResult")
public class ServiceResult<T> implements IServiceResult {

    @XmlElement(name = "content")
    protected T content;
    @XmlElement(name = "msg")
    protected String msg;

    public ServiceResult() {

    }

    public ServiceResult(T content) {
        this();
        this.content = content;
    }

    public ServiceResult(T content, String msg) {
        this(content);
        this.msg = msg;
    }

    @Override
    public T getContent() {
        return content;
    }
}
