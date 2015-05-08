package models;

import play.data.validation.Required;
import play.db.jpa.Blob;
import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ProjectMap extends Model {

    public String name;

    public Blob file;

    public ProjectMap(String name, Blob file) {
        this.name = name;
        this.file = file;
    }
}
