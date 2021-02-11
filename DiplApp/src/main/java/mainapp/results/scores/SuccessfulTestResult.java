package mainapp.results.scores;

public class SuccessfulTestResult extends TestResult {
    private double score = Double.NaN;

	public void setScore(double testScore) {
        score = testScore;
	}

	public double getScore() {
		return score;
	}
}
