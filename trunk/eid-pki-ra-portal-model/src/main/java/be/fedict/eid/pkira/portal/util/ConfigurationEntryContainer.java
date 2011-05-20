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

package be.fedict.eid.pkira.portal.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.pkira.privatews.EIDPKIRAPrivateServiceClient;

/**
 * @author Bram Baeyens
 *
 */
@Name(ConfigurationEntryContainer.NAME)
@Scope(ScopeType.APPLICATION)
public class ConfigurationEntryContainer implements Serializable {
	
	private static final long serialVersionUID = 2931481314588085666L;
	
	public static final String NAME = "be.fedict.eid.pkira.portal.configurationEntryContainer";
	
	private static final String VALIDITY_PERIODS_KEY = "VALIDITY_PERIODS";
	private static final String DSS_SERVLET_KEY = "DSS_SERVLET";
	private static final String IDP_DESTINATION_KEY = "IDP_DESTINATION";
	private static final String IDP_FINGERPRINT_KEY = "IDP_FINGERPRINT";
	private static final String IDP_MAXTIMEOFFSET_KEY = "IDP_MAXTIMEOFFSET";
	
	
	@In(value = EIDPKIRAPrivateServiceClient.NAME, create = true)
	private EIDPKIRAPrivateServiceClient eidpkiraPrivateServiceClient;

	public List<SelectItem> getValidityPeriods() {
		String[] validityPeriodValues = findValidityPeriods();
		List<SelectItem> validityPeriods = new ArrayList<SelectItem>();
		for (String validityPeriod : validityPeriodValues) {
			validityPeriods.add(new SelectItem(validityPeriod, validityPeriod));
		}
		return validityPeriods;
	}
	
	public String getDssServletUrl() {
		return findConfigurationEntry(DSS_SERVLET_KEY);
	}

	private String[] findValidityPeriods() {
		String periods = findConfigurationEntry(VALIDITY_PERIODS_KEY);
		if (periods != null) {
			return periods.split(",");
		}
		return null;
	}
	
	private String findConfigurationEntry(String entryKey) {
		return eidpkiraPrivateServiceClient.findConfigurationEntry(entryKey).getEntryValue();
	}

	public String getIDPDestination() {
		return findConfigurationEntry(IDP_DESTINATION_KEY);
	}

	public String getFingerprint() {
		return findConfigurationEntry(IDP_FINGERPRINT_KEY);
	}
	
	public int getMaxTimeOffset(){
		return Integer.parseInt(findConfigurationEntry(IDP_MAXTIMEOFFSET_KEY));
	}
}
