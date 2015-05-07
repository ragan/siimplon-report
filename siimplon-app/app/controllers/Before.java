package controllers;

import models.Project;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

@OnApplicationStart
public class Before extends Job {

    @Override
    public void doJob() throws Exception {
        if (Project.count() == 0) {
            Fixtures.loadModels("data.yml");
        }
    }

}
