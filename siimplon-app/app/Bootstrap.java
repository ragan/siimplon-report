import controllers.Projects;
import models.Project;
import models.ProjectMap;
import play.db.jpa.Blob;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.libs.MimeTypes;
import play.test.Fixtures;
import play.vfs.VirtualFile;

import javax.activation.MimeType;
import java.io.File;
import java.io.FileInputStream;

@OnApplicationStart
public class Bootstrap extends Job {

    private String fileName;

    @Override
    public void doJob() throws Exception {
        Fixtures.deleteAllModels();
        Fixtures.loadModels("data.yml");
        // ~~~~~~~~~
        fileName = "sample.kml";
        VirtualFile vf = VirtualFile.fromRelativePath("conf/sample.kml");
        File realFile = vf.getRealFile();
        Blob blob = new Blob();
        blob.set(new FileInputStream(realFile), MimeTypes.getContentType(realFile.getName()));
        // ~~~~~~~~~
        Project project = Project.all().first();
        ProjectMap projectMap = new ProjectMap("Google test map", blob);
        project.projectMaps.add(projectMap);
        project.save();
    }

}
