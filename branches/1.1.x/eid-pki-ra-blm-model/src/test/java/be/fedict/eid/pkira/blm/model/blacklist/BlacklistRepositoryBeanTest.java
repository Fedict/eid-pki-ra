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

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.model.DatabaseTest;
import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.blm.model.usermgmt.Registration;
import be.fedict.eid.pkira.blm.model.usermgmt.User;

import static org.testng.Assert.assertEquals;

public class BlacklistRepositoryBeanTest extends DatabaseTest {

    private BlacklistRepositoryBean repository;

    @BeforeClass
    public void setup() {
        repository = new BlacklistRepositoryBean();
        repository.setEntityManager(getEntityManager());
    }

    @Test
    public void canGetBlacklistItems() {
        User user = getEntityManager().find(User.class, 2001);
        CertificateDomain certificateDomain = getEntityManager().find(CertificateDomain.class, 1001);

        Registration registration = new Registration();
        registration.setRequester(user);
        registration.setCertificateDomain(certificateDomain);

        List<BlacklistItem> items = repository.getAllBlacklistItemsForRegistration(registration);
        assertEquals(items.size(), 3);
    }

}