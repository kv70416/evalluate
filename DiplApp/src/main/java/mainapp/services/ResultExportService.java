package mainapp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

import mainapp.configurations.ModuleConfiguration;
import mainapp.modules.factoryinterfaces.IResultExportModuleFactory;
import mainapp.modules.interfaces.IResultExportModule;
import mainapp.results.ratings.DuplicateRatings;
import mainapp.results.scores.FailedStudentResult;
import mainapp.results.scores.FailedTestResult;
import mainapp.results.scores.Results;
import mainapp.results.scores.StudentResult;
import mainapp.results.scores.SuccessfulStudentResult;
import mainapp.results.scores.SuccessfulTestResult;
import mainapp.results.scores.TestResult;

public class ResultExportService extends ModuleService<IResultExportModule> {

    public ResultExportService() {
        factories = new ArrayList<>();
        ServiceLoader<IResultExportModuleFactory> ffLoader = ServiceLoader.load(IResultExportModuleFactory.class);
        for (IResultExportModuleFactory factory : ffLoader) {
            factories.add(factory);
        }

        instances = new ArrayList<>();
    }

    @Override
    public boolean isConfigurationValid(ModuleConfiguration config) {
        return true;
    }

    public void setExportPath(int index, String path) {
        if (badIndex(index))
            return;
        instances.get(index).setExportPath(path);
    }

    public boolean runExport(int index, Results results, DuplicateRatings ratings) {
        if (badIndex(index))
            return false;

        IResultExportModule.IResultAccess accessClass = new IResultExportModule.IResultAccess() {

            @Override
            public List<String> getScoredStudents() {
                return new ArrayList<>(results.getStudentResults().keySet());
            }

            @Override
            public List<String> getScoringModules(String student) {
                StudentResult res = results.getStudentResults().get(student);
                if (res instanceof SuccessfulStudentResult) {
                    return new ArrayList<>(((SuccessfulStudentResult) res).getModuleScores().keySet());
                }
                return new ArrayList<>();
            }

            @Override
            public List<String> getScoringTests(String student, String module) {
                StudentResult res = results.getStudentResults().get(student);
                if (res instanceof SuccessfulStudentResult) {
                    return new ArrayList<>(((SuccessfulStudentResult) res).getTestResults(module).keySet());
                }
                return new ArrayList<>();
            }

            @Override
            public double getStudentScore(String student) {
                StudentResult res = results.getStudentResults().get(student);
                if (res instanceof SuccessfulStudentResult) {
                    return ((SuccessfulStudentResult) res).getTotalScore();
                }
                return Double.NaN;
            }

            @Override
            public String getStudentMessage(String student) {
                StudentResult res = results.getStudentResults().get(student);
                if (res instanceof FailedStudentResult) {
                    return ((FailedStudentResult) res).getFailMessage();
                }
                return null;
            }

            @Override
            public double getStudentModuleScore(String student, String module) {
                StudentResult res = results.getStudentResults().get(student);
                if (res instanceof SuccessfulStudentResult) {
                    return ((SuccessfulStudentResult) res).getModuleScores().get(module);
                }
                return Double.NaN;
            }

            @Override
            public double getStudentTestScore(String student, String module, String test) {
                StudentResult res = results.getStudentResults().get(student);
                if (res instanceof SuccessfulStudentResult) {
                    TestResult testRes = ((SuccessfulStudentResult) res).getTestResults(module).get(test);
                    if (testRes instanceof SuccessfulTestResult) {
                        return ((SuccessfulTestResult) testRes).getScore();
                    }
                }
                return Double.NaN;
            }

            @Override
            public String getStudentTestMessage(String student, String module, String test) {
                StudentResult res = results.getStudentResults().get(student);
                if (res instanceof SuccessfulStudentResult) {
                    TestResult testRes = ((SuccessfulStudentResult) res).getTestResults(module).get(test);
                    if (testRes instanceof SuccessfulTestResult) {
                        return ((SuccessfulTestResult) testRes).getMessage();
                    }
                    if (testRes instanceof FailedTestResult) {
                        return ((FailedTestResult) testRes).getFailMessage();
                    }                    
                }
                return null;
            }

            @Override
            public List<String> getRatedStudents() {
                return new ArrayList<>(ratings.getStudentRatings().keySet());
            }

            @Override
            public List<String> getRatingModules(String student) {
                return new ArrayList<>(ratings.getStudentRatings().get(student).getModuleRatings().keySet());
            }

            @Override
            public List<String> getComparedStudents(String student, String module) {
                return new ArrayList<>(ratings.getStudentRatings().get(student).getPairwiseRatings().get(module).stream()
                    .map(pr -> pr.getComparisonStudent())
                    .collect(Collectors.toList())
                );
            }

            @Override
            public double getStudentRating(String student) {
                return ratings.getStudentRatings().get(student).getTotalRating();
            }

            @Override
            public double getStudentModuleRating(String student, String module) {
                return ratings.getStudentRatings().get(student).getModuleRatings().get(module);
            }

            @Override
            public double getComparisonRating(String student, String module, String comparedStudent) {
                return ratings.getStudentRatings().get(student).getPairwiseRatings().get(module).stream()
                    .filter(pr -> pr.getComparisonStudent().equals(comparedStudent))
                    .findFirst()
                    .get()
                    .getComparisonRating();
            }

        };

        instances.get(index).setAccessInterface(accessClass);

        return instances.get(index).export();
    } 
}
