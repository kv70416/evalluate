package mainapp.results;

public class FailedStudentResult extends StudentResult {
    private String failMessage = null;

    public FailedStudentResult() {
    }

	public void setFailMessage(String message) {
        this.failMessage = message;
	}

	public String getFailMessage() {
		return failMessage;
	}
}
