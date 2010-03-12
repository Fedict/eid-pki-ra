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

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.model.domain.Certificate;


/**
 * @author Jan Van den Bergh
 *
 */
public class MailTemplateBeanTest {
	
	@Mock
	private MailSender mailSender;
	
	private Log log = Logging.getLog(MailTemplateBean.class);

	private MailTemplateBean bean;
	
	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);
		
		bean = new MailTemplateBean();
		
		bean.setLog(log);
		bean.setMailSender(mailSender);
		bean.initialize();
	}

	@Test
	public void testSendTemplatedMail() throws IOException {		
		Map<String, Object> parameters = Collections.singletonMap("certificate", (Object) createCertificate());
		String[] recipients = new String[]{"test@example.com"};
		bean.sendTemplatedMail("sendCertificateMail.ftl", parameters, recipients, new byte[0], "test", "test");
		
		verify(mailSender).sendMail(isA(Mail.class));
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
		return certificate;
	}

}
