package be.fedict.eid.pkira.xkmsws.keyinfo;

import java.util.Collections;
import java.util.Map;

import be.fedict.eid.pkira.xkmsws.XKMSClientException;

public abstract class KeyProviderBase implements KeyProvider {

	private Map<String, String> parameters = Collections.emptyMap();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	protected String getParameter(String parameterName, boolean required) throws XKMSClientException {
		String result = parameters.get(parameterName);
		if (result == null && required) {
			throw new XKMSClientException("Missing parameter: " + parameterName);
		}
		return result;
	}

}
