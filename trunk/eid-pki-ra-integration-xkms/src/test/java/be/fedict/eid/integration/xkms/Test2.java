package be.fedict.eid.integration.xkms;

import java.io.File;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

public class Test2 {

	@Test
	public void testJan() throws Exception {
		String message = FileUtils.readFileToString(new File("/Users/jan/Desktop/Create cert request.xml"));

		HttpClient client = new HttpClient();
		client.getHostConfiguration().setProxy("localhost", 8008);

		PostMethod method = new PostMethod("http://64.18.25.193/soap/kitoshi");
		method.setRequestEntity(new StringRequestEntity(message, "text/xml", "utf-8"));
		int result = client.executeMethod(method);

		System.out.println(result);
	}
}
