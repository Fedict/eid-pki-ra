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

package be.fedict.eid.pkira.blm.model.blacklist;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomainQuery;
import be.fedict.eid.pkira.blm.model.framework.ValidatingEntityHome;
import be.fedict.eid.pkira.blm.model.usermgmt.User;
import be.fedict.eid.pkira.blm.model.usermgmt.UserQuery;

@Name(BlacklistItemHome.NAME)
public class BlacklistItemHome extends ValidatingEntityHome<BlacklistItem> {

	public static final String NAME = "be.fedict.eid.pkira.blm.blacklistItemHome";

    @In(value= CertificateDomainQuery.NAME, create = true)
    private CertificateDomainQuery certificateDomainQuery;

    @In(value= UserQuery.NAME, create = true)
    private UserQuery userQuery;
	
	@Override
	protected String getUpdatedMessageKey() {
		return "blacklistitem.updated";
	}
	
	@Override
	protected String getCreatedMessageKey() {
		return "blacklistitem.created";
	}

    public List<SelectItem> getAllCertificateDomains() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        for (CertificateDomain certificateDomain : certificateDomainQuery.getResultList()) {
            result.add(new SelectItem(certificateDomain, certificateDomain.getName(), certificateDomain.getDnExpressionShortened()));
        }

        return result;
    }

    public List<SelectItem> getAllUsers() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        for (User user : userQuery.getResultList()) {
            result.add(new SelectItem(user, user.getName()));
        }

        return result;
    }
}
