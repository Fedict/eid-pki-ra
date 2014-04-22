/*
 * eID PKI RA Project.
 * Copyright (C) 2010-2014 FedICT.
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

package be.fedict.eid.pkira.blm.model.framework;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.dss.client.DigitalSignatureServiceClient;
import be.fedict.eid.pkira.blm.model.ca.CertificateAuthority;
import be.fedict.eid.pkira.blm.model.config.ConfigurationEntryKey;
import be.fedict.eid.pkira.blm.model.config.ConfigurationEntryQuery;
import be.fedict.eid.pkira.xkmsws.XKMSClient;
import be.fedict.eid.pkira.xkmsws.XKMSLogger;

@Name(WebserviceLocator.NAME)
@Scope(ScopeType.STATELESS)
public class WebserviceLocator {

	public static final String NAME = "be.fedict.eid.pkira.blm.webserviceLocator";

	@In(value = ConfigurationEntryQuery.NAME, create = true)
	private ConfigurationEntryQuery configurationEntryQuery;
	
	@In(value=XKMSLogger.NAME, create=true)
	private XKMSLogger xkmsLogger;

	public DigitalSignatureServiceClient getDigitalSignatureServiceClient() {
		return new DigitalSignatureServiceClient(findWebserviceUrl(ConfigurationEntryKey.DSS_WS_CLIENT));
	}

	public XKMSClient getXKMSClient(CertificateAuthority ca) {
		XKMSClient xkmsClient = new XKMSClient(ca.getXkmsUrl(), ca.getParametersAsMap());
		xkmsClient.setXkmsLogger(xkmsLogger);
		return xkmsClient;
	}

	private String findWebserviceUrl(ConfigurationEntryKey configurationEntryKey) {
		return configurationEntryQuery.findByEntryKey(configurationEntryKey).getValue();
	}

	public String getIDPDestination() {
		return findWebserviceUrl(ConfigurationEntryKey.IDP_DESTINATION);
	}

	public String getSPReturnURL() {
		return findWebserviceUrl(ConfigurationEntryKey.SP_RETURN_URL_ADMIN);
	}
}
