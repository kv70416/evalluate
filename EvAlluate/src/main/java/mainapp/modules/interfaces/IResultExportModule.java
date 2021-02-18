package mainapp.modules.interfaces;

import java.util.List;

public interface IResultExportModule extends IModule {
    public void setAccessInterface(IResultAccess i);

    public void setExportPath(String path);
    
    public boolean export();

    public interface IResultAccess {
        public List<String> getScoredStudents();
        public List<String> getScoringModules(String student);
        public List<String> getScoringTests(String student, String module);
        public double getStudentScore(String student);
        public String getStudentMessage(String student);
        public double getStudentModuleScore(String student, String module);
        public double getStudentTestScore(String student, String module, String test);
        public String getStudentTestMessage(String student, String module, String test);

        public List<String> getRatedStudents();
        public List<String> getRatingModules(String student);
        public List<String> getComparedStudents(String student, String module);
        public double getStudentRating(String student);
        public double getStudentModuleRating(String student, String module);
        public double getComparisonRating(String student, String module, String comparedStudent);
    }
}
