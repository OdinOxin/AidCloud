package de.odinoxin.aidcloud.translation;

import de.odinoxin.aidcloud.plugins.Recordable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Translation")
public class Translation implements Recordable {

    @Id
    @GeneratedValue
    private int id;
    private String sys;
    private String deu;
    private String usa;

    public Translation() {

    }

    public Translation(int id) {
        this();
        this.id = id;
    }

    public Translation(String sys) {
        this(0, sys, null, null);
    }

    public Translation(String sys, String deu, String usa) {
        this(0, sys, deu, usa);
    }

    public Translation(int id, String sys, String deu, String usa) {
        this(id);
        this.sys = sys;
        this.deu = deu == null ? sys : deu;
        this.usa = usa == null ? sys : usa;
    }

    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        throw new NotImplementedException();
    }

    public String getSys() {
        return sys;
    }

    public void setSys(String sys) {
        this.sys = sys;
    }

    public String getDeu() {
        return deu;
    }

    public void setDeu(String deu) {
        this.deu = deu;
    }

    public String getUsa() {
        return usa;
    }

    public void setUsa(String usa) {
        this.usa = usa;
    }

    @Override
    public Object clone() {
        return null;
    }
}
