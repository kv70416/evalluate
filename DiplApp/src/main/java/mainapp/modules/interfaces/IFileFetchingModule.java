package mainapp.modules.interfaces;

import java.util.List;

public interface IFileFetchingModule extends IModule {
    public List<String> getAllStudents();
    public boolean fetchSourceFiles(String student, String targetSourcePath);
}
