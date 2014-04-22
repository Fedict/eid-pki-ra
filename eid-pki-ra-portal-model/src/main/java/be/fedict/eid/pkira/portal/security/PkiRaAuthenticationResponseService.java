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

package be.fedict.eid.pkira.portal.security;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.common.security.AbstractPkiRaAuthenticationResponseService;
import be.fedict.eid.pkira.portal.util.ConfigurationEntryContainer;

@Name(PkiRaAuthenticationResponseService.NAME)
public class PkiRaAuthenticationResponseService extends AbstractPkiRaAuthenticationResponseService {

	@In(value = ConfigurationEntryContainer.NAME, create=true)
	protected ConfigurationEntryContainer configurationEntryContainer;
	
	@Override
	public String[] getFingerprints() {
		return configurationEntryContainer.getIdpFingerprints();
	}

	@Override
	public int getMaximumTimeOffset() {
		return configurationEntryContainer.getMaxTimeOffset();
	}

}
