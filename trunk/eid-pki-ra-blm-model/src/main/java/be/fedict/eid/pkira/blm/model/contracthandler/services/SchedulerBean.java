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
package be.fedict.eid.pkira.blm.model.contracthandler.services;

import java.math.BigInteger;
import java.security.cert.CertPathValidatorException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.annotations.async.Expiration;
import org.jboss.seam.async.QuartzTriggerHandle;
import org.jboss.seam.log.Log;

import be.fedict.eid.pkira.blm.model.ca.CertificateChainCertificate;
import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.blm.model.contracts.Certificate;
import be.fedict.eid.pkira.blm.model.contracts.ContractRepository;
import be.fedict.eid.pkira.blm.model.mail.MailTemplate;
import be.fedict.eid.pkira.blm.model.usermgmt.Registration;
import be.fedict.eid.pkira.crypto.CertificateInfo;
import be.fedict.eid.pkira.crypto.CertificateParser;
import be.fedict.eid.pkira.crypto.CertificateParserImpl;
import be.fedict.eid.pkira.crypto.CryptoException;
import be.fedict.trust.BelgianTrustValidatorFactory;
import be.fedict.trust.TrustValidator;

/**
 * @author Jan Van den Bergh
 */
@Name(SchedulerBean.NAME)
@Scope(ScopeType.STATELESS)
public class SchedulerBean {

	public static final String NAME = "be.fedict.eid.pkira.blm.scheduler";

	@In(value = MailTemplate.NAME, create = true)
	private MailTemplate mailTemplate;

	@In(create = true)
	private QuartzTriggerHandle timer;
	
	@In(value=ContractRepository.NAME, create=true)
	private ContractRepository contractRepository;
	
	@Logger
	private Log log;

	@Asynchronous
	@Transactional(TransactionPropagationType.REQUIRED)
	public QuartzTriggerHandle scheduleNotifcation(@Expiration Date when, String issuer, BigInteger serialNumber) {
		// Get the certificate
		Certificate certificate = contractRepository.findCertificate(issuer, serialNumber);
		if (certificate==null) {
			log.warn("Certificate not found for issuer {0} and serial number {1}.", issuer, serialNumber);
			return timer;
		}
	
		try {
			List<X509Certificate> certificatePath = new ArrayList<X509Certificate>();
			CertificateParser certificateParser = new CertificateParserImpl();
			
			CertificateChainCertificate certificateChainCertificate = certificate.getCertificateChainCertificate();
			if(certificateChainCertificate != null){
				while(certificateChainCertificate != null){
					CertificateInfo certificateInfo = certificateParser.parseCertificate(certificateChainCertificate.getCertificateData());
					certificatePath.add(certificateInfo.getCertificate());
					certificateChainCertificate = certificateChainCertificate.getIssuer();
				}
				TrustValidator createTrustValidator = BelgianTrustValidatorFactory.createTrustValidator();
				createTrustValidator.isTrusted(certificatePath);
			}
		} catch (CertPathValidatorException e) {
			log.warn("CertificatePath error for this certificate", issuer, serialNumber);
			return timer;
		} catch (CryptoException e) {
			log.warn("Certificate not found for issuer {0} and serial number {1}.", issuer, serialNumber);
			return timer;
		}
		
		
		// Get the recipients
		CertificateDomain certificateDomain = certificate.getCertificateDomain();
		List<String> emailsList = new ArrayList<String>();
		for(Registration registration: certificateDomain.getRegistrations()) {
			emailsList.add(registration.getEmail());
		}
		String[] emails = emailsList.toArray(new String[0]);
		
		// Create the parameter map 
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("certificate", certificate);

		mailTemplate.sendTemplatedMail("certificateNearlyExpired.ftl", parameters, emails);

		return timer;
	}
}
