package be.fedict.eid.pkira.xkmsws;

/**
 * Interface used by the XKMS client to log things that happened.
 * 
 * @author jan
 */
public interface XKMSLogger {

	public enum XKMSMessageType {
		REQUEST, REVOCATION
	}

	String NAME = "be.fedict.eid.pkira.xkms.xkmsLogger";

	void logError(XKMSMessageType messageType, String requestMessage, byte[] responseMessage, Throwable t);

	void logSuccesfulInteraction(XKMSMessageType messageType, String requestMessage, byte[] responseMessage);

}
