package mainapp.results;

import java.util.HashMap;
import java.util.Map;

public class SuccessfulStudentResult extends StudentResult {
	private double totalScore = Double.NaN;
    private Map<String, Double> totalModuleScores = null;
    private Map<String, Map<String, TestResult>> moduleTestResults = null;

    public SuccessfulStudentResult() {
        totalModuleScores = new HashMap<>();
        moduleTestResults = new HashMap<>();
    }


    public Map<String, Double> getModuleScores() {
		return totalModuleScores;
	}

    public Map<String, TestResult> getTestResults(String module) {
		return moduleTestResults.get(module);
	}



	public void setModuleScore(String module, double score) {
        totalModuleScores.put(module, score);
	}

	public void addTestResult(String module, String testName, TestResult testRes) {
        Map<String, TestResult> testResults = moduleTestResults.get(module);
        if (testResults == null) {
            moduleTestResults.put(module, new HashMap<>());
            testResults = moduleTestResults.get(module);
        }
        TestResult oldResult = testResults.get(testName);
        if (oldResult == null || oldResult instanceof SuccessfulTestResult || testRes instanceof FailedTestResult) {
            testResults.put(testName, testRes);
        }
	}

	public void calculateTotalScore() {
        // TODO aggregation
        totalScore = 0.;
	}


}
