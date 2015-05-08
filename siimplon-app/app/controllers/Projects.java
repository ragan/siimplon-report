package controllers;

import models.Project;
import models.ProjectMap;
import play.db.jpa.JPABase;

import java.util.List;

public class Projects extends Application {

    public static void all() {
        List<Project> projects = Project.all().fetch();
        render(projects);
    }

    public static void show(Long id) {
        Project project = Project.findById(id);

        List<ProjectMap> maps = project.projectMaps;
        render(project, maps);
    }

    public static void add() {
        render();
    }

    public static void create(String name, String description) {
        Project project = new Project(name, description).save();
        show(project.id);
    }

    public static void delete(Long id) {
        Project project = Project.findById(id);
        project.delete();
        all();
    }

    public static void attachFile(Long id) {

    }
}
