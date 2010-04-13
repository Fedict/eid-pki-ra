/*
 * eID PKI RA Project.
 * Copyright (C) 2010 FedICT.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version
 * 3.0 as published by the Free Software Foundation.
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, see
 * http://www.gnu.org/licenses/.
 */
package be.fedict.eid.integration.setup.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.IOUtils;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import be.fedict.eid.integration.util.ConnectionDetailsReader;

/**
 * @author Jan Van den Bergh
 */
public class SetupDatabase {

	@BeforeSuite
	@Parameters("context")
	public void setupDatabase(String context) throws ClassNotFoundException, SQLException, IOException {
		// Setup the database
		loadJdbcDriver();
		
		// Get a connection
		Connection connection = openConnection();
		
		// Execute the update
		try {
			Statement statement = connection.createStatement();
			
			try {
				String sql = readSql(context);
				statement.execute(sql);
			} finally {
				statement.close();
			}
			
		} finally {
			connection.close();
		}
	}

	private void loadJdbcDriver() throws ClassNotFoundException {
		String driverClass = ConnectionDetailsReader.getConnectionProperty("be.fedict.eid.integration.db.driver");
		Class.forName(driverClass);
	}

	private Connection openConnection() throws SQLException {
		String jdbcUrl = ConnectionDetailsReader.getConnectionProperty("be.fedict.eid.integration.db.jdbcUrl");
		String user = ConnectionDetailsReader.getConnectionProperty("be.fedict.eid.integration.db.userName");;
		String password = ConnectionDetailsReader.getConnectionProperty("be.fedict.eid.integration.db.password");;
		return DriverManager.getConnection(jdbcUrl, user, password);
	}

	protected String readSql(String context) throws IOException {
		String resourceName = ConnectionDetailsReader.getConnectionProperty("be.fedict.eid.integration.db.sql." + context);
		InputStream input = getClass().getClassLoader().getResourceAsStream(resourceName);
		
		return IOUtils.toString(input);
	}
}
