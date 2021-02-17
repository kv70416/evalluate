# EvAlluate

***A modular system for secure evaluation of programming solutions.***


EvAlluate is a work-in-progress, standalone Java-based code evaluation application, designed to work with an arbitrary set of modules, each providing its own logic of how an individual step of the evaluation process should be carried out.

EvAlluate is currently desgined to support five types of modules:
- **File Fetching Modules**, which implement the logic of retrieving source files of solutions from a desired source
- **Code Compilation Modules**, which handle environment set-up, compilation and execution of the analyzed code
- **Solution Scoring Modules**, which define the manner in which the analyzed solutions are scored
- **Duplicate Detection Modules**, which implement the logic of assessing authenticity of individual solutions
- **Result Export Modules** (optional), which allow for exporting the evaluation outcomes to an external destination.

Individual modules are implemented as Java class libraries, containing implementations of module interfaces for their corresponding type, which are defined as a part of the main EvAlluate application. The main application loads any module libraries available in its classpath via the Java ServiceLoader class.

Examples of implemented modules can be found [here](https://github.com/kv70416/evalluate-modules).
