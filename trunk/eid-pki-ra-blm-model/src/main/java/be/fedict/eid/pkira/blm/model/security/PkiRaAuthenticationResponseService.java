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

package be.fedict.eid.pkira.blm.model.security;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.blm.model.config.ConfigurationEntryKey;
import be.fedict.eid.pkira.blm.model.config.ConfigurationEntryQuery;
import be.fedict.eid.pkira.common.security.AbstractPkiRaAuthenticationResponseService;

@Name(PkiRaAuthenticationResponseService.NAME)
public class PkiRaAuthenticationResponseService extends AbstractPkiRaAuthenticationResponseService {

	@In(value = ConfigurationEntryQuery.NAME, create = true)
	private ConfigurationEntryQuery configurationEntryQuery;

	@Override
	public String[] getFingerprints() {
		String fingerprint1 = configurationEntryQuery.findByEntryKey(ConfigurationEntryKey.IDP_FINGERPRINT).getValue();
		String fingerprint2 = configurationEntryQuery.findByEntryKey(ConfigurationEntryKey.IDP_FINGERPRINT_ROLLOVER)
				.getValue();

		if (StringUtils.isBlank(fingerprint1) && StringUtils.isBlank(fingerprint2)) {
			return new String[0];
		}
		
		if (StringUtils.isBlank(fingerprint1)) {
			return new String[]
				{ fingerprint2 };
		}
		if (StringUtils.isBlank(fingerprint2)) {
			return new String[]
				{ fingerprint1 };
		}
		return new String[]
			{ fingerprint1, fingerprint2 };
	}

	@Override
	public int getMaximumTimeOffset() {
		String value = configurationEntryQuery.findByEntryKey(ConfigurationEntryKey.IDP_MAXTIMEOFFSET).getValue();
		if (value == null || value.isEmpty()) {
			return 0;
		}
		return Integer.parseInt(value);
	}

}
