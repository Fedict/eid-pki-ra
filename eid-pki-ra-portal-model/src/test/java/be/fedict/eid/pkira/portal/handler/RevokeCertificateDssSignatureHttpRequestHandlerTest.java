package be.fedict.eid.pkira.portal.handler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.testng.annotations.Test;

import be.fedict.eid.pkira.contracts.EIDPKIRAContractsClient;
import be.fedict.eid.pkira.generated.contracts.CertificateRevocationResponseType;
import be.fedict.eid.pkira.generated.contracts.ResultType;
import be.fedict.eid.pkira.publicws.EIDPKIRAServiceClient;

public class RevokeCertificateDssSignatureHttpRequestHandlerTest 
		extends AbstractDssSignatureHttpRequestHandlerTest<RevokeCertificateDssSignatureHttpRequestHandler, 
				CertificateRevocationResponseType> {
	
	@Override
	protected void initHandler() {
		handler =  new RevokeCertificateDssSignatureHttpRequestHandler() {

			protected EIDPKIRAServiceClient getServiceClient() {
				return serviceClient;
			}

			protected EIDPKIRAContractsClient getContractsClient() {
				return contractsClient;
			}
			
			protected CertificateRevocationResponseType unmarshall(String result) {
				return certificateResponse;
			}
		};		
	}
	
	protected void initMocks() throws Exception {
		super.initMocks();
		certificateResponse = mock(CertificateRevocationResponseType.class);
	}
	
	@Test
	public void successfulRequest() throws Exception {
		when(request.getParameter(SIGNATURE_STATUS_PARAMETER)).thenReturn("OK");
		when(certificateResponse.getResult()).thenReturn(ResultType.SUCCESS);
		handler.handleRequest(request, response);
		verify(response).sendRedirect("/page/csr/dssSignSucces.seam");
	}
	
	@Test
	public void signatureNotOk() throws Exception {
		when(request.getParameter(SIGNATURE_STATUS_PARAMETER)).thenReturn("NOT_OK");
		handler.handleRequest(request, response);
		verify(response).sendRedirect("/page/csr/dssSignError.seam");
	}
	
	@Test
	public void backendError() throws Exception {
		when(request.getParameter(SIGNATURE_STATUS_PARAMETER)).thenReturn("OK");
		when(certificateResponse.getResult()).thenReturn(ResultType.BACKEND_ERROR);
		handler.handleRequest(request, response);
		verify(response).sendRedirect("/page/csr/dssSignError.seam");
	}
	
	@Test
	public void exception() throws Exception {
		when(request.getParameter(SIGNATURE_STATUS_PARAMETER)).thenReturn("OK");
		when(certificateResponse.getResult()).thenThrow(new NullPointerException());
		handler.handleRequest(request, response);
		verify(response).sendRedirect("/page/csr/dssSignError.seam");
	}
}
