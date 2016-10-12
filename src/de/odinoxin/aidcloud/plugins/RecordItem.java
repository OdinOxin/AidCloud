package de.odinoxin.aidcloud.plugins;

public abstract class RecordItem {

    protected int id;

    public RecordItem() {

    }

    public RecordItem(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
