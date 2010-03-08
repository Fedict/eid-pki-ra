package be.fedict.eid.pkira.portal.handler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.testng.annotations.BeforeMethod;

import be.fedict.eid.pkira.contracts.EIDPKIRAContractsClient;
import be.fedict.eid.pkira.publicws.EIDPKIRAServiceClient;

public abstract class AbstractDssSignatureHttpRequestHandlerTest<S, T> {
	
	protected static final String SIGNATURE_RESPONSE_PARAMETER = "SignatureResponse";
	protected static final String SIGNATURE_STATUS_PARAMETER = "SignatureStatus";
	
	protected S handler;
	protected EIDPKIRAServiceClient serviceClient;
	protected EIDPKIRAContractsClient contractsClient;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected T certificateResponse;
	
	@BeforeMethod
	protected void setUp() throws Exception {
		initMocks();
		initHandler();
	}

	protected abstract void initHandler();

	protected void initMocks() throws Exception {
		serviceClient = mock(EIDPKIRAServiceClient.class);
		contractsClient = mock(EIDPKIRAContractsClient.class);
		request = mock(HttpServletRequest.class);
		when(request.getContextPath()).thenReturn("");
		when(request.getParameter(SIGNATURE_RESPONSE_PARAMETER)).thenReturn("");
		response = mock(HttpServletResponse.class);
	}
}
