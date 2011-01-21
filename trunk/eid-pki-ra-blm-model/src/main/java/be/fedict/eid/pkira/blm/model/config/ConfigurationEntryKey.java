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

package be.fedict.eid.pkira.blm.model.config;

/**
 * @author Bram Baeyens
 *
 */
public enum ConfigurationEntryKey {
	
	MAIL_SERVER(ConfigurationEntryType.STRING, "configurationEntry.key.mailServer", "localhost"), 
	MAIL_PORT(ConfigurationEntryType.DIGITS, "configurationEntry.key.mailPort", "25"),
	MAIL_USER(ConfigurationEntryType.OPTIONAL_STRING, "configurationEntry.key.mailUser", ""),
	MAIL_PASSWORD(ConfigurationEntryType.OPTIONAL_STRING, "configurationEntry.key.mailPassword", ""),
	MAIL_PROTOCOL(ConfigurationEntryType.STRING, "configurationEntry.key.mailProtocol", "smtp"),
	//NOTIFICATION_MAIL_DAYS(ConfigurationEntryType.DIGITS, "configurationEntry.key.notificationMailDays"), 
	NOTIFICATION_MAIL_MINUTES(ConfigurationEntryType.COMMA_SEPARATED_DIGITS, "configurationEntry.key.notificationMailMinutes", "43200"), 
	VALIDITY_PERIODS(ConfigurationEntryType.COMMA_SEPARATED_DIGITS, "configurationEntry.key.validityPeriods", "15,27"), 
	DSS_SERVLET(ConfigurationEntryType.URL, "configurationEntry.key.dssServlet", "https://www.e-contract.be/eid-dss/protocol/simple"), 
	DSS_WS_CLIENT(ConfigurationEntryType.URL, "configurationEntry.key.dssWsClient", "https://www.e-contract.be/eid-dss/dss"), 
	IDP_DESTINATION(ConfigurationEntryType.URL, "configurationEntry.key.idpDestination", "https://www.e-contract.be/eid-idp/protocol/saml2-auth-ident");
	
	private final ConfigurationEntryType type;
	private final String message;
	private String defaultValue;
	
	private ConfigurationEntryKey(ConfigurationEntryType type, String message, String defaultValue) {
		this.type = type;
		this.message = message;
		this.defaultValue = defaultValue;
	}
	
	public ConfigurationEntryType getConfigurationEntryType() {
		return type;
	}
	
	public String getMessage() {
		return message;
	}

	
	public ConfigurationEntryType getType() {
		return type;
	}

	
	public String getDefaultValue() {
		return defaultValue;
	}
}
