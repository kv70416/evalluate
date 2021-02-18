package mainapp.results.scores;

public class FailedTestResult extends TestResult {
    private String failMessage = null;

	public void setFailMessage(String message) {
        failMessage = message;
	}

	public String getFailMessage() {
		return failMessage;
	}
}
