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
package be.fedict.eid.pkira.blm.model.mail;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.errorhandling.ErrorLogger;
import be.fedict.eid.pkira.blm.model.config.ConfigurationEntryQuery;
import be.fedict.eid.pkira.blm.model.contracts.Certificate;
import be.fedict.eid.pkira.blm.model.contracts.CertificateType;
import be.fedict.eid.pkira.blm.model.usermgmt.User;

/**
 * @author Jan Van den Bergh
 */
public class MailTemplateBeanTest {

	@Mock
	private MailSender mailSender;

	private MailTemplateBean bean;

	@Mock
	private ConfigurationEntryQuery configurationEntryQuery;

	@Mock
	private ErrorLogger errorLogger;

	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);

		bean = new MailTemplateBean();

		bean.setConfigurationEntryQuery(configurationEntryQuery);
		bean.setMailSender(mailSender);
		bean.setErrorLogger(errorLogger);
		bean.initialize();
		
		when(configurationEntryQuery.getAsMap()).thenReturn(Collections.singletonMap("HOMEPAGE_URL", "{URL}"));
	}

	@Test
	public void testSendTemplatedMail() throws IOException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("certificate", createCertificate());
		parameters.put("user", createUser());

		String[] recipients = new String[]
			{ "test@example.com" };
		bean.sendTemplatedMail("sendCertificateMail.ftl", parameters, recipients, new byte[0], "test", "test", "en");

		verify(mailSender).sendMail(isA(Mail.class));
	}

	/**
	 * @return
	 */
	private User createUser() {
		User user = new User();
		user.setFirstName("First");
		user.setLastName("Last");
		user.setNationalRegisterNumber("123");

		return user;
	}

	private Certificate createCertificate() {
		Certificate certificate = new Certificate();
		certificate.setIssuer("issuer");
		certificate.setRequesterName("requester");
		certificate.setSerialNumber(BigInteger.TEN);
		certificate.setDistinguishedName("distinguishedName");
		certificate.setValidityEnd(new Date());
		certificate.setValidityStart(new Date());
		certificate.setX509("X509");
		certificate.setCertificateType(CertificateType.SERVER);
		return certificate;
	}

}
