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

package be.fedict.eid.pkira.portal.signing;

import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;

import be.fedict.eid.dss.protocol.simple.client.SignatureResponse;
import be.fedict.eid.dss.protocol.simple.client.SignatureResponseProcessor;
import be.fedict.eid.pkira.contracts.EIDPKIRAContractsClient;
import be.fedict.eid.pkira.contracts.XmlMarshallingException;
import be.fedict.eid.pkira.generated.contracts.ResponseType;
import be.fedict.eid.pkira.generated.contracts.ResultType;
import be.fedict.eid.pkira.portal.util.ConfigurationEntryContainer;
import be.fedict.eid.pkira.publicws.EIDPKIRAServiceClient;

public abstract class AbstractDssSigningHandler<T extends ResponseType> {

	@Logger
	private Log log;

	@In(value = "be.fedict.eid.pkira.portal.signatureResponseProcessor", create = true)
	private SignatureResponseProcessor signatureResponseProcessor;

	@In(value = "be.fedict.eid.pkira.portal.configurationEntryContainer", create = true)
	private ConfigurationEntryContainer configurationEntryContainer;

	private static final String SUCCESSFUL_REDIRECT = "success";

	public static final String EVENT_CERTIFICATE_LIST_CHANGED = "CertificateListChanged";

	public String handleDssRequest() {
		String redirectStatus = null;
		T serviceClientResponse = null;
		try {
			// Extract the signature response
			String signatureRequestId = "request-" + java.util.UUID.randomUUID().toString();
			SignatureResponse signatureResponse = signatureResponseProcessor.process(getRequest(), getTarget(),
					getBase64encodedSignatureRequest(), signatureRequestId, null);

			// Get dssCertificate digest and allowed fingerprints
			String actualServiceFingerprint = null;
			String serviceCertificate = getRequest().getParameter(
					SignatureResponseProcessor.SERVICE_CERTIFICATE_PARAMETER_PREFIX + "1");
			if (serviceCertificate != null) {
				byte[] certificateData = Base64.decodeBase64(serviceCertificate);
				byte[] certificateFingerPrint = DigestUtils.sha(certificateData);
				actualServiceFingerprint = Hex.encodeHexString(certificateFingerPrint);
			}
			log.info("Actual service fingerprint: " + actualServiceFingerprint);

			String[] fingerprintConfig = configurationEntryContainer.getDssFingerprints();
			if (fingerprintConfig == null || fingerprintConfig.length == 0) {
				log.warn("No DSS fingerprints configured");
			} else {
				if (actualServiceFingerprint == null) {
					log.warn("No dssCertificate in DSS response");
					throw new SecurityException("Missing dssCertificate in DSS response");
				}

				boolean ok = false;
				for (String fingerprint : fingerprintConfig) {
					log.info("Allowed service fingerprint: " + fingerprint);
					ok |= fingerprint != null && fingerprint.equalsIgnoreCase(actualServiceFingerprint);
				}

				if (!ok) {
					log.error("Signatures not correct.");
					throw new SecurityException("Signatures not correct.");
				}
			}

			// Call the contract service
			byte[] contract = signatureResponse.getDecodedSignatureResponse();
			String result = invokeServiceClient(new String(contract));
			serviceClientResponse = unmarshall(result);
			getFacesMessages().addFromResourceBundle("contract.status." + serviceClientResponse.getResult().name(),
					serviceClientResponse.getResultMessage());

			if (Events.exists()) {
				Events.instance().raiseEvent(EVENT_CERTIFICATE_LIST_CHANGED);
			}

			if (ResultType.SUCCESS.equals(serviceClientResponse.getResult())) {
				redirectStatus = SUCCESSFUL_REDIRECT;
			}
		} catch (Exception e) {
			getFacesMessages().addFromResourceBundle("validator.error.sign");
			log.info("<<< handleRequest: exception");
		}

		return handleRedirect(redirectStatus, serviceClientResponse);
	}

	protected String handleRedirect(String redirectStatus, T serviceClientResponse) {
		if (SUCCESSFUL_REDIRECT.equals(redirectStatus)) {
			return SUCCESSFUL_REDIRECT;
		} else {
			if (serviceClientResponse != null) {
				getFacesMessages().add(Severity.ERROR, serviceClientResponse.getResultMessage());
			}
			return "error";
		}
	}

	protected abstract String invokeServiceClient(String signedRequest) throws Exception;

	protected abstract T unmarshall(String result) throws XmlMarshallingException;

	protected String nullSafeGetRequestParameter(HttpServletRequest request, String parameterName)
			throws ServletException {
		log.info(">>> nullSafeGetRequestParameter(parameterName[" + parameterName + "])");
		String parameter = request.getParameter(parameterName);
		if (parameter == null) {
			throw new ServletException(parameterName + " parameter not present");
		}
		log.info("<<< nullSafeGetRequestParameter: " + parameter);
		return parameter;
	}

	/**
	 * Make sure to call this method in a Seam contextual context.
	 */
	protected EIDPKIRAServiceClient getServiceClient() {
		return (EIDPKIRAServiceClient) Component.getInstance(EIDPKIRAServiceClient.NAME);
	}

	/**
	 * Make sure to call this method in a Seam contextual context.
	 */
	protected EIDPKIRAContractsClient getContractsClient() {
		return (EIDPKIRAContractsClient) Component.getInstance(EIDPKIRAContractsClient.NAME);
	}

	/**
	 * Make sure to call this method in a Seam contextual context.
	 */
	protected FacesMessages getFacesMessages() {
		return (FacesMessages) Component.getInstance(FacesMessages.class, true);
	}

	protected HttpServletRequest getRequest() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		return request;
	}

	protected abstract String getTarget();

	protected abstract String getBase64encodedSignatureRequest();

	/**
	 * Injects the log.
	 */
	public void setLog(Log log) {
		this.log = log;
	}

	public void setSignatureResponseProcessor(SignatureResponseProcessor signatureResponseProcessor) {
		this.signatureResponseProcessor = signatureResponseProcessor;
	}

	public void setConfigurationEntryContainer(ConfigurationEntryContainer configurationEntryContainer) {
		this.configurationEntryContainer = configurationEntryContainer;
	}
}
