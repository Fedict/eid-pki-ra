package be.fedict.eid.integration.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import be.fedict.eid.integration.BaseSeleniumTestCase;

public class ConnectionDetailsReader {

	private static Properties properties;

	public static String getConnectionProperty(String propertyName) {
		synchronized (ConnectionDetailsReader.class) {
			if (properties == null) {
				InputStream inputStream = BaseSeleniumTestCase.class
						.getResourceAsStream("connection-details.properties");
				properties = new Properties();

				try {
					properties.load(inputStream);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
		
		return properties.getProperty(propertyName);
	}

}
