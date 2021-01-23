package mainapp.modules.interfaces;

import java.util.List;

public interface ICodeCompilationModule extends IModule{
    public String sourceTargetDirectoryPath(String student);
    public String inputTargetDirectoryPath(String student);
    public String programOutputFilePath(String student);

    public boolean initialize(List<String> student);
    public boolean compileSource(String student);
    public boolean runProgram(String student);
    public boolean cleanUp(List<String> student);
}
