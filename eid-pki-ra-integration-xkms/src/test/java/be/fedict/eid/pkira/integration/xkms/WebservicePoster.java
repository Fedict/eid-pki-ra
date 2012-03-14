package be.fedict.eid.pkira.integration.xkms;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.FileUtils;

public class WebservicePoster {

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.out.println("Arguments: <inputFile> <outputFile>");
			System.exit(1);
		}

		WebservicePoster poster = new WebservicePoster();
		poster.postMessage(args[0], args[1]);
	}

	private void postMessage(String inputFile, String outputFile) throws IOException {
		// Load the properties
		Properties properties = new Properties();
		properties.load(getClass().getClassLoader().getResourceAsStream("xkms-integration.properties"));

		// Create the HTTP Client
		HttpClient client = new HttpClient();
		if (properties.containsKey("http.proxyHost")) {
			String proxyHost = properties.getProperty("http.proxyHost");
			int proxyPort = Integer.parseInt(properties.getProperty("http.proxyPort"));
			client.getHostConfiguration().setProxy(proxyHost, proxyPort);
		}

		// Post the message
		String inputMessage = FileUtils.readFileToString(new File(inputFile));
		String url = properties.getProperty("xkms.url");
		PostMethod method = new PostMethod(url);
		method.setRequestEntity(new StringRequestEntity(inputMessage, "text/xml", "utf-8"));
		int result = client.executeMethod(method);
		System.err.println("Result: " + result);

		// Write its result
		byte[] outputMessage = method.getResponseBody();
		FileUtils.writeByteArrayToFile(new File(outputFile), outputMessage);
		System.out.write(outputMessage);
		System.out.println();
	}
}
