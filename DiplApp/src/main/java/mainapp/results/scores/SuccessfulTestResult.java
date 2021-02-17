package mainapp.results.scores;

public class SuccessfulTestResult extends TestResult {
    private double score = Double.NaN;
	private String message = null;

	public void setScore(double testScore) {
        score = testScore;
	}

	public double getScore() {
		return score;
	}

	public void setMessage(String msg) {
		message = msg;
	}

	public String getMessage() {
		return message;
	}
}
