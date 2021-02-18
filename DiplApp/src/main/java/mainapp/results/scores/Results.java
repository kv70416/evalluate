package mainapp.results.scores;

import java.util.HashMap;
import java.util.Map;

import mainapp.results.AggregationType;

public class Results {
    
    private Map<String, StudentResult> studentResults;
    
    
    public Results() {
        studentResults = new HashMap<>();
    }


    public Map<String, StudentResult> getStudentResults() {
        return studentResults;
    }


    public void calculateTotalScore(String student, AggregationType type) {
        StudentResult res = studentResults.get(student);
        if (res == null || res instanceof FailedStudentResult) {
            return;
        }
        ((SuccessfulStudentResult) res).calculateTotalScore(type);
    }

    public void setStudentModuleScore(String student, String module, double score) {
        StudentResult res = studentResults.get(student);
        if (res instanceof FailedStudentResult) {
            return;
        }
        if (res == null) {
            res = new SuccessfulStudentResult();
            studentResults.put(student, res);
        }
        ((SuccessfulStudentResult) res).setModuleScore(module, score);
    }
    
    public void setStudentModuleTestScores(String student, String module, Map<String, Double> scores) {
        StudentResult res = studentResults.get(student);
        if (res instanceof FailedStudentResult) {
            return;
        }
        if (res == null) {
            res = new SuccessfulStudentResult();
            studentResults.put(student, res);
        }
        
        for (String testName : scores.keySet()) {
            double testScore = scores.get(testName);

            SuccessfulTestResult testRes = new SuccessfulTestResult();
            testRes.setScore(testScore);

            ((SuccessfulStudentResult) res).addTestResult(module, testName, testRes);
        }
    }
    

    public void setStudentFail(String student, String failMessage) {
        StudentResult res = studentResults.get(student);
        if (res == null || res instanceof SuccessfulStudentResult) {
            res = new FailedStudentResult();
            studentResults.put(student, res);
        }
        ((FailedStudentResult) res).setFailMessage(failMessage);
    }


	public void setStudentModuleTestFail(String student, String module, String test, String failMessage) {
        StudentResult res = studentResults.get(student);
        if (res instanceof FailedStudentResult) {
            return;
        }
        if (res == null) {
            res = new SuccessfulStudentResult();
            studentResults.put(student, res);
        }

        FailedTestResult testRes = new FailedTestResult();
        testRes.setFailMessage(failMessage);

        ((SuccessfulStudentResult) res).addTestResult(module, test, testRes);
	}

}