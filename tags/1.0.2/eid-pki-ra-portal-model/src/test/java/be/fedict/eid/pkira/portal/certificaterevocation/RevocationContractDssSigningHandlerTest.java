package be.fedict.eid.pkira.portal.certificaterevocation;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import javax.servlet.http.HttpServletRequest;

import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.testng.Assert;
import org.testng.annotations.Test;

import be.fedict.eid.dss.protocol.simple.client.SignatureResponse;
import be.fedict.eid.dss.protocol.simple.client.SignatureResponseProcessorException;
import be.fedict.eid.pkira.contracts.EIDPKIRAContractsClient;
import be.fedict.eid.pkira.generated.contracts.CertificateRevocationResponseType;
import be.fedict.eid.pkira.generated.contracts.ResultType;
import be.fedict.eid.pkira.portal.signing.AbstractDssSigningHandlerTest;
import be.fedict.eid.pkira.publicws.EIDPKIRAServiceClient;

public class RevocationContractDssSigningHandlerTest extends
		AbstractDssSigningHandlerTest<RevocationContractDssSigningHandler, CertificateRevocationResponseType> {

	@Override
	protected void initHandler() {
		handler = new RevocationContractDssSigningHandler() {

			@Override
			protected EIDPKIRAServiceClient getServiceClient() {
				return serviceClient;
			}

			@Override
			protected EIDPKIRAContractsClient getContractsClient() {
				return contractsClient;
			}

			@Override
			protected CertificateRevocationResponseType unmarshall(String result) {
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
	}

	@Override
	protected void initMocks() throws Exception {
		super.initMocks();
		certificateResponse = mock(CertificateRevocationResponseType.class);
	}

	@Test
	public void successfulRequest() throws Exception {
		when(request.getParameter(SIGNATURE_STATUS_PARAMETER)).thenReturn("OK");
		when(signatureRequestProcessor.process(isA(HttpServletRequest.class), eq(TARGET), eq(BASE64_REQUEST)))
				.thenReturn(new SignatureResponse(new byte[0], certificate));
		when(certificateResponse.getResult()).thenReturn(ResultType.SUCCESS);
		String outcome = handler.handleDssRequest();
		Assert.assertEquals(outcome, "success");
	}

	@Test
	public void signatureNotOk() throws Exception {
		when(request.getParameter(SIGNATURE_STATUS_PARAMETER)).thenReturn("NOT_OK");
		when(signatureRequestProcessor.process(isA(HttpServletRequest.class), eq(TARGET), eq(BASE64_REQUEST)))
				.thenThrow(new SignatureResponseProcessorException("ERROR"));
		String outcome = handler.handleDssRequest();
		Assert.assertEquals(outcome, "error");
	}

	@Test
	public void backendError() throws Exception {
		when(request.getParameter(SIGNATURE_STATUS_PARAMETER)).thenReturn("OK");
		when(signatureRequestProcessor.process(isA(HttpServletRequest.class), eq(TARGET), eq(BASE64_REQUEST)))
				.thenReturn(new SignatureResponse(new byte[0], certificate));
		when(certificateResponse.getResult()).thenReturn(ResultType.BACKEND_ERROR);
		String outcome = handler.handleDssRequest();
		Assert.assertEquals(outcome, "error");
	}
}
