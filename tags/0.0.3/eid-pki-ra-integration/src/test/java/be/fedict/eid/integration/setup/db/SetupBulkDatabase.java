/**
 * eID PKI RA Project. 
 * Copyright (C) 2010 FedICT. 
 * 
 * This is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License version 
 * 3.0 as published by the Free Software Foundation. 
 * 
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

/**
 * @author Bram Baeyens
 *
 */
public class SetupBulkDatabase extends SetupDatabase {

	protected String readSql(String context) throws IOException {				
		return generateSQL();
	}

	private String generateSQL() {
		return new StringBuilder()
				.append(generateDeleteSQL())
				.append("INSERT INTO USER (USER_ID, LAST_NAME, FIRST_NAME, NATIONAL_REGISTER_NUMBER, IS_ADMIN) VALUES(5001, 'User', 'Test', '99999999999', 1);\n")
				.append(generateCertificateAuthoritySQL())
				.append(generateCeritficateDomainSQL())
				.append(generateConfigurationEntrySQL())
				.toString();				
	}

	private StringBuilder generateConfigurationEntrySQL() {
		return new StringBuilder()
				.append("INSERT INTO CONFIGURATION (ENTRY_KEY, ENTRY_VALUE) VALUES('MAIL_SERVER', 'a');\n")
				.append("INSERT INTO CONFIGURATION (ENTRY_KEY, ENTRY_VALUE) VALUES('MAIL_PORT', 'b');\n")
				.append("INSERT INTO CONFIGURATION (ENTRY_KEY, ENTRY_VALUE) VALUES('NOTIFICATION_MAIL_DAYS', 'c');\n")
				.append("INSERT INTO CONFIGURATION (ENTRY_KEY, ENTRY_VALUE) VALUES('VALIDITY_PERIODS', 'd');\n")
				.append("INSERT INTO CONFIGURATION (ENTRY_KEY, ENTRY_VALUE) VALUES('DSS_SERVLET', 'e');\n");
	}

	private Object generateCeritficateDomainSQL() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 150; i++) {
			String sequence = addLeadingZero(i);
			builder.append("INSERT INTO CertificateDomain (CERTIFICATE_DOMAIN_NAME, DN_EXPRESSION, CLIENTCERT, SERVERCERT, CODECERT, CA_ID) VALUES ('Name")
					.append(sequence)
					.append("', 'DN")
					.append(sequence)
					.append("', 1, 0, 1, ")
					.append(5001 + ");\n");
		}
		return builder;
	}
	
	private StringBuilder generateCertificateAuthoritySQL(){
		StringBuilder builder = new StringBuilder();
		
		builder.append("INSERT INTO CA (CA_ID, NAME, XKMS_URL) VALUES (5001, 'CertiPost CA', 'http://xkms-url.be/xkms')");
		
		return builder;
	}

	private StringBuilder generateDeleteSQL() {
		return new StringBuilder()
				.append("DELETE FROM CERTIFICATE;\n")
				.append("DELETE FROM CONTRACT;\n")
				.append("DELETE FROM REGISTRATION;\n")
				.append("DELETE FROM USER;\n")
				.append("DELETE FROM CERTIFICATEDOMAIN;\n")
				.append("DELETE FROM CA;\n")
				.append("DELETE FROM CONFIGURATION;\n");
	}

	private String addLeadingZero(int i) {
		if (i < 10) {
			return "00" + i;
		} else if (i < 100) {
			return "0" + i;
		} else {
			return "" + i;
		}
	}
}
