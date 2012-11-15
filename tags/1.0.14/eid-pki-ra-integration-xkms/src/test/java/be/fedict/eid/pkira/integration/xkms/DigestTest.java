package be.fedict.eid.pkira.integration.xkms;

import static org.testng.Assert.assertEquals;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.testng.annotations.Test;
import org.w3c.dom.Document;

import be.fedict.eid.pkira.crypto.xmlsign.XmlDocumentSigner;
import be.fedict.eid.pkira.xkmsws.keyinfo.KeyStoreKeyProvider;
import be.fedict.eid.pkira.xkmsws.signing.XkmsXmlDocumentSigner;

/**
 * Class to test the digest algorithm.
 */
public class DigestTest {

	@Test
	public void testDigest() throws Exception {
		// Load the properties
		Properties properties = new Properties();
		properties.load(getClass().getClassLoader().getResourceAsStream("xkms-integration.properties"));
		Map<String, String> parameters = new HashMap<String, String>();
		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			parameters.put((String) entry.getKey(), (String) entry.getValue());
		}

		// Load the document
		InputStream input = getClass().getResourceAsStream("/known_message.xml");
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(input);

		// Get key info
		KeyStoreKeyProvider provider = new KeyStoreKeyProvider();
		provider.setParameters(parameters);

		// Sign the document
		new XmlDocumentSigner().signXMLDocument(document, provider.getCertificate(),
				provider.getPrivateKey(), XkmsXmlDocumentSigner.ELEMENT_TO_APPEND_TO, XkmsXmlDocumentSigner.ELEMENT_TO_SIGN);
		writeDocument(document);

		// Extract the digest value
		String digestValue = document.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "DigestValue")
				.item(0).getTextContent();

		assertEquals(digestValue, "iRYw7DMWtMEdxcHPMg+dNU6srBU=");
	}

	private void writeDocument(Document doc) throws Exception {
		// Prepare the DOM document for writing
		Source source = new DOMSource(doc);
		// Prepare the output file
		Result result = new StreamResult(System.out);
		// Write the DOM document to the file
		Transformer xformer = TransformerFactory.newInstance().newTransformer();
		xformer.transform(source, result);
	}
}
