package models;

import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Entity;

@Entity
public class Project extends Model {

    @Required
    @MinSize(4)
    public String name;

    public Project(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Project: %s", name);
    }
}
