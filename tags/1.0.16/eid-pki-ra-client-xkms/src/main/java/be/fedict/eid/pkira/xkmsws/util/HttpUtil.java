package be.fedict.eid.pkira.xkmsws.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import be.fedict.eid.pkira.xkmsws.XKMSClientException;

public class HttpUtil {

	private final HttpClient client;
	private final String endpointAddress;

	public HttpUtil(String endPointAddress, Map<String, String> parameters) {
		this.endpointAddress = endPointAddress;

		client = new HttpClient();
		if (parameters.containsKey("http.proxyHost")) {
			String proxyHost = parameters.get("http.proxyHost");
			int proxyPort = Integer.parseInt(parameters.get("http.proxyPort"));
			client.getHostConfiguration().setProxy(proxyHost, proxyPort);
		}
	}

	public byte[] postMessage(String message) throws XKMSClientException {
		try {
			PostMethod method = new PostMethod(endpointAddress);
			method.setRequestEntity(new StringRequestEntity(message, "text/xml", "utf-8"));
			int resultCode = client.executeMethod(method);
			if (resultCode != 200) {
				throw new XKMSClientException("Error calling XKMS service. Got back status code " + resultCode);
			}

			return method.getResponseBody();
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e); // shouldn't happen
		} catch (HttpException e) {
			throw new XKMSClientException("Error calling XKMS service.", e);
		} catch (IOException e) {
			throw new XKMSClientException("Error calling XKMS service.", e);
		}
	}
}
