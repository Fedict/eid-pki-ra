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

package be.fedict.eid.pkira.portal.registration;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.common.security.EIdUserCredentials;
import be.fedict.eid.pkira.generated.privatews.RegistrationWS;
import be.fedict.eid.pkira.portal.framework.DataTableWSQuery;

/**
 * @author Bram Baeyens
 *
 */
@Name(RegistrationWSQuery.NAME)
public class RegistrationWSQuery extends DataTableWSQuery {

	private static final long serialVersionUID = -6479453928366362421L;
	
	public static final String NAME = "be.fedict.eid.pkira.portal.registrationWSQuery";
	
	@In
	private EIdUserCredentials credentials;
	
	@In(value=RegistrationMapper.NAME, create=true)
	private RegistrationMapper registrationMapper;
	
	private List<Registration> registrations;
	
	public List<Registration> getFindByUserRRN() {
		if (registrations == null) {		
			registrations = new ArrayList<Registration>();
			List<RegistrationWS> registrationWSList = getServiceClient().findRegistrationsByUserRRN(credentials.getUser().getRRN());
			for (RegistrationWS registrationWS : registrationWSList) {
				registrations.add(registrationMapper.map(registrationWS));
			}
		}
		return registrations;
	}
}
