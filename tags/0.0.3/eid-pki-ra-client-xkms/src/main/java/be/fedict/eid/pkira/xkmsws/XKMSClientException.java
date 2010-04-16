package be.fedict.eid.pkira.xkmsws;

public class XKMSClientException extends Exception {

	private static final long serialVersionUID = 2057707877854913104L;

	public XKMSClientException() {
	}

	public XKMSClientException(String message) {
		super(message);
	}

	public XKMSClientException(Throwable cause) {
		super(cause);
	}

	public XKMSClientException(String message, Throwable cause) {
		super(message, cause);
	}

}
