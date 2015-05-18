package models;

import play.data.validation.Required;
import play.db.jpa.Blob;
import play.db.jpa.Model;

import javax.persistence.Entity;

@Entity
public class Project extends Model {

    @Required
    public String name;

    public String description;

    public Blob file;

    public Project(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("Project: %s", name);
    }
}
