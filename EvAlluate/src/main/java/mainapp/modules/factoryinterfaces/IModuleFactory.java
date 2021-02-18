package mainapp.modules.factoryinterfaces;

import mainapp.modules.interfaces.IModule;

public interface IModuleFactory<IModuleType extends IModule> {
    public String moduleID();
    public String moduleName();
    public String moduleDescription();
    public IModuleType newModuleInstance();
}
