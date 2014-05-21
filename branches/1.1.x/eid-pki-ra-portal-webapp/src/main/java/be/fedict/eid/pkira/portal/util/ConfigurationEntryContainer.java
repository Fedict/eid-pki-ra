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

package be.fedict.eid.pkira.portal.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.pkira.generated.privatews.ConfigurationEntryKeyWS;
import be.fedict.eid.pkira.privatews.EIDPKIRAPrivateServiceClient;

/**
 * @author Bram Baeyens
 */
@Name(ConfigurationEntryContainer.NAME)
@Scope(ScopeType.APPLICATION)
public class ConfigurationEntryContainer implements Serializable {

	private static final long serialVersionUID = 2931481314588085666L;

	public static final String NAME = "be.fedict.eid.pkira.portal.configurationEntryContainer";

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
		return findConfigurationEntry(ConfigurationEntryKeyWS.DSS_SERVLET);
	}

	private String[] findValidityPeriods() {
		String periods = findConfigurationEntry(ConfigurationEntryKeyWS.VALIDITY_PERIODS);
		if (periods != null) {
			return periods.split(",");
		}
		return null;
	}

	private String findConfigurationEntry(ConfigurationEntryKeyWS entryKey) {
		return eidpkiraPrivateServiceClient.findConfigurationEntry(entryKey).getEntryValue();
	}

	public String getIDPDestination() {
		return findConfigurationEntry(ConfigurationEntryKeyWS.IDP_DESTINATION);
	}

	public String[] getIdpFingerprints() {
		String fingerprint1 = findConfigurationEntry(ConfigurationEntryKeyWS.IDP_FINGERPRINT);
		String fingerprint2 = findConfigurationEntry(ConfigurationEntryKeyWS.IDP_FINGERPRINT_ROLLOVER);

		return selectNotEmptyValues(fingerprint1, fingerprint2);
	}
	
	public String[] getDssFingerprints() {
		String fingerprint1 = findConfigurationEntry(ConfigurationEntryKeyWS.DSS_FINGERPRINT);
		String fingerprint2 = findConfigurationEntry(ConfigurationEntryKeyWS.DSS_FINGERPRINT_ROLLOVER);

		return selectNotEmptyValues(fingerprint1, fingerprint2);
	}

	private String[] selectNotEmptyValues(String... fingerPrints) {
		List<String> values = new ArrayList<String>();
		
		for(String fingerPrint: fingerPrints) {
			if (!StringUtils.isBlank(fingerPrint)) {
				values.add(fingerPrint);
			}
		}
		
		return values.toArray(new String[0]);
	}

	public int getMaxTimeOffset() {
		return Integer.parseInt(findConfigurationEntry(ConfigurationEntryKeyWS.IDP_MAXTIMEOFFSET));
	}

	public String getSPReturnUrl() {
		return findConfigurationEntry(ConfigurationEntryKeyWS.SP_RETURN_URL_PORTAL);
	}

    public String getCsvExportFieldSeparator() {
        return findConfigurationEntry(ConfigurationEntryKeyWS.CSV_EXPORT_FIELD_SEPARATOR);
    }
}
