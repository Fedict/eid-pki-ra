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

package be.fedict.eid.pkira.portal.certificaterequest;

import javax.servlet.http.HttpServletRequest;

import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.testng.Assert;
import org.testng.annotations.Test;

import be.fedict.eid.dss.protocol.simple.client.SignatureResponse;
import be.fedict.eid.dss.protocol.simple.client.SignatureResponseProcessorException;
import be.fedict.eid.pkira.contracts.EIDPKIRAContractsClient;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningResponseType;
import be.fedict.eid.pkira.generated.contracts.ResultType;
import be.fedict.eid.pkira.portal.certificate.CertificateWSHome;
import be.fedict.eid.pkira.portal.signing.AbstractDssSigningHandlerTest;
import be.fedict.eid.pkira.publicws.EIDPKIRAServiceClient;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestContractDssSigningHandlerTest extends
		AbstractDssSigningHandlerTest<RequestContractDssSigningHandler, CertificateSigningResponseType> {

	@Override
	protected void initHandler() {
		handler = new RequestContractDssSigningHandler() {

			@Override
			protected EIDPKIRAServiceClient getServiceClient() {
				return serviceClient;
			}

			@Override
			protected EIDPKIRAContractsClient getContractsClient() {
				return contractsClient;
			}

			@Override
			protected CertificateSigningResponseType unmarshall(String result) {
				return certificateResponse;
			}

			@Override
			protected FacesMessages getFacesMessages() {
				return facesMessages;
			}

			@Override
			protected HttpServletRequest getRequest() {
				return request;
			}

			@Override
			public CertificateWSHome getCertificateWSHome() {
				return certificateWSHome;
			}

			@Override
			protected String getTarget() {
				return TARGET;
			}

			@Override
			protected String getBase64encodedSignatureRequest() {
				return BASE64_REQUEST;
			}

		};

		handler.setLog(mock(Log.class));
		handler.setSignatureResponseProcessor(signatureRequestProcessor);
		handler.setConfigurationEntryContainer(configurationEntryContainer);
	}

	@Override
	protected void initMocks() throws Exception {
		super.initMocks();
		certificateResponse = mock(CertificateSigningResponseType.class);
	}

	@Test
	public void successfullRequest() throws Exception {
		when(request.getParameter(SIGNATURE_STATUS_PARAMETER)).thenReturn("OK");
		when(signatureRequestProcessor.process(isA(HttpServletRequest.class), eq(TARGET), eq(BASE64_REQUEST), anyString(), anyString()))
				.thenReturn(new SignatureResponse(new byte[0], null,dssCertificate));
		when(certificateResponse.getResult()).thenReturn(ResultType.SUCCESS);
		// when(certificateWSHome.getInstance()).thenReturn("success");
		String outcome = handler.handleDssRequest();
		Assert.assertEquals(outcome, "success");
	}

	@Test
	public void signatureNotOk() throws Exception {
		when(request.getParameter(SIGNATURE_STATUS_PARAMETER)).thenReturn("NOT_OK");
		when(signatureRequestProcessor.process(isA(HttpServletRequest.class), eq(TARGET), eq(BASE64_REQUEST), anyString(), anyString()))
				.thenThrow(new SignatureResponseProcessorException("ERROR"));
		String outcome = handler.handleDssRequest();
		Assert.assertEquals(outcome, "error");
	}

	@Test
	public void backendError() throws Exception {
		when(request.getParameter(SIGNATURE_STATUS_PARAMETER)).thenReturn("OK");
		when(signatureRequestProcessor.process(isA(HttpServletRequest.class), eq(TARGET), eq(BASE64_REQUEST), anyString(), anyString()))
				.thenReturn(new SignatureResponse(new byte[0], null, dssCertificate));
		when(certificateResponse.getResult()).thenReturn(ResultType.BACKEND_ERROR);
		String outcome = handler.handleDssRequest();
		Assert.assertEquals(outcome, "error");
	}
}
