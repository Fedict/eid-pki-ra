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
package be.fedict.eid.pkira.portal.util;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.ServletLifecycle;

import be.fedict.eid.pkira.contracts.EIDPKIRAContractsClient;
import be.fedict.eid.pkira.privatews.EIDPKIRAPrivateServiceClient;
import be.fedict.eid.pkira.publicws.EIDPKIRAServiceClient;

/**
 * @author Jan Van den Bergh
 */
@Name(WebserviceFactory.NAME)
@Scope(ScopeType.APPLICATION)
public class WebserviceFactory {

	public static final String NAME = "be.fedict.eid.pkira.portal.webserviceFactory";

	@Factory(value = EIDPKIRAServiceClient.NAME, scope = ScopeType.APPLICATION)
	public EIDPKIRAServiceClient getPublicServiceClient() {
		String url = getServiceUrlFromServletContext() + "/EIDPKIRAService";
		
		EIDPKIRAServiceClient result = new EIDPKIRAServiceClient();		
		result.setServiceUrl(url);
		return result;
	}
	
	@Factory(value = EIDPKIRAPrivateServiceClient.NAME, scope = ScopeType.APPLICATION)
	public EIDPKIRAPrivateServiceClient getPrivateServiceClient() {
		String url = getServiceUrlFromServletContext() + "/EIDPKIRAPrivateService";
		
		EIDPKIRAPrivateServiceClient result = new EIDPKIRAPrivateServiceClient();		
		result.setServiceUrl(url);
		return result;
	}
	
	@Factory(value = EIDPKIRAContractsClient.NAME, scope = ScopeType.APPLICATION)
	public EIDPKIRAContractsClient getContractsClient() {
		return new EIDPKIRAContractsClient();
	}

	private String getServiceUrlFromServletContext() {
		return ServletLifecycle.getServletContext().getInitParameter("be.fedict.eid.pkira.blm.services");
	}
}
