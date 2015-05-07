package controllers;

import models.Project;
import play.*;
import play.db.jpa.GenericModel;
import play.mvc.*;

import java.util.List;

public class Application extends Controller {

    public static void index() {
        List<Project> projects = Project.all().fetch();
        render(projects);
    }

    public static void help() {
        render();
    }

}