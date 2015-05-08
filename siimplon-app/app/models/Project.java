package models;

import play.data.validation.Required;
import play.db.jpa.Blob;
import play.db.jpa.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Project extends Model {

    @Required
    public String name;

    public String description;

    @OneToMany(cascade = CascadeType.ALL)
    public List<ProjectMap> projectMaps;

    public Project(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
