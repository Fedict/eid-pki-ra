package be.fedict.eid.pkira.xkmsws;


/**
 * Interface used by the XKMS client to log things that happened.
 * @author jan
 */
public interface XKMSLogger {

	void logError(String type, String requestMessage, byte[] responseMessage, Throwable t);

	void logSuccesfulInteraction(String type, String requestMessage, byte[] responseMessage);

}
