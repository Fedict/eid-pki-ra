package be.fedict.eid.integration.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import be.fedict.eid.integration.BaseSeleniumTestCase;

public class ConnectionDetailsReader {

	public static String getConnectionProperty(String propertyName) {
		InputStream inputStream = BaseSeleniumTestCase.class.getResourceAsStream("connection-details.properties");
		Properties properties = new Properties();
		try {
			properties.load(inputStream);
			return properties.getProperty(propertyName);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
