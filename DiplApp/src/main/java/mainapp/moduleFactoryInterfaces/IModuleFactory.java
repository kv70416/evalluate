package mainapp.moduleFactoryInterfaces;

import mainapp.moduleInterfaces.IModule;

public interface IModuleFactory<IModuleType extends IModule> {
    public String moduleID();
    public String moduleName();
    public String moduleDescription();
    public IModuleType newModuleInstance();
}
