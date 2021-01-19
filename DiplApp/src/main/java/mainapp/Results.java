package mainapp;

import java.util.ArrayList;
import java.util.List;

public class Results {
    
    private List<StudentScore> ssResults;
    
    
    public Results() {
        ssResults = new ArrayList<>();
    }
    
    
    public void setStudentModuleScore(String student, String module, double score) {
        getStudentScore(student).getModuleScore(module).totalModuleScore = score;
    }
    
    public void setStudentTestScores(String student, String module, List<String> tests, List<Double> scores) {
        if (tests.size() != scores.size()) {
            System.out.println("AMOUNTS OF TESTS AND TEST SCORES DO NOT MATCH");
            return;
        }
        
        List<TestScore> testScores = getStudentScore(student).getModuleScore(module).testScores;
        for (int i = 0; i < tests.size(); i++) {
            String testName = tests.get(i);
            double testScore = scores.get(i);
            
            boolean testFound = false;
            for (TestScore ts : testScores) {
                if (ts.testName.equals(testName)) {
                    ts.testScore = testScore;
                    testFound = true;
                    break;
                }
            }
            
            if (!testFound) {
                testScores.add(new TestScore(testName, testScore));
            }
        }
    }
    
    public void calculateTotalScore(String student) {
        // TODO aggregation
        getStudentScore(student).totalScore = 7;
    }
    
    
    public List<StudentScore> getStudentScores() {
        return ssResults;
    }
    
    
    private StudentScore getStudentScore(String student) {
        if (ssResults == null) return null;
        for (StudentScore ss : ssResults) {
            if (ss.studentName.equals(student)) return ss;
        }
        StudentScore studentScore = new StudentScore(student);
        ssResults.add(studentScore);
        return studentScore;
    }
    
    
    public class StudentScore {
        private String studentName;
        private double totalScore;
        private List<ModuleScore> moduleScores;
        
        private StudentScore(String student) {
            studentName = student;
            totalScore = Double.NaN;
            moduleScores = new ArrayList<>();
        }
        
        private ModuleScore getModuleScore(String moduleName) {
            if (moduleScores == null) return null;
            for (ModuleScore ms : moduleScores) {
                if (ms.moduleName.equals(moduleName)) return ms;
            }
            ModuleScore moduleScore = new ModuleScore(moduleName);
            moduleScores.add(moduleScore);
            return moduleScore;
        }
        
        public String getStudent() {
            return studentName;
        }
        
        public double getTotalScore() {
            return totalScore;
        }
        
        public List<ModuleScore> getModuleScores() {
            return moduleScores;
        }
    }
    
    public class ModuleScore {
        private String moduleName;
        private double totalModuleScore;
        private List<TestScore> testScores;
        
        private ModuleScore(String module) {
            moduleName = module;
            totalModuleScore = Double.NaN;
            testScores = new ArrayList<>();
        }
        
        public String getModuleName() {
            return moduleName;
        }
        
        public double getModuleScore() {
            return totalModuleScore;
        }
        
        public List<TestScore> getTestScores() {
            return testScores;
        }
    }
    
    public class TestScore {
        private String testName;
        private double testScore;
        
        private TestScore(String name, double score) {
            testName = name;
            testScore = score;
        }
        
        public String getTestName() {
            return testName;
        }
        
        public double getTestScore() {
            return testScore;
        }
    }
    
    
}
