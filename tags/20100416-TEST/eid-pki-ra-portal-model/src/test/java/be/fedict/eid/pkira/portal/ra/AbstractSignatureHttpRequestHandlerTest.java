package be.fedict.eid.pkira.portal.ra;

import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.faces.FacesMessages;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;

import be.fedict.eid.pkira.contracts.EIDPKIRAContractsClient;
import be.fedict.eid.pkira.publicws.EIDPKIRAServiceClient;

public abstract class AbstractSignatureHttpRequestHandlerTest<S, T> {
	
	protected static final String SIGNATURE_RESPONSE_PARAMETER = "SignatureResponse";
	protected static final String SIGNATURE_STATUS_PARAMETER = "SignatureStatus";
	
	protected S handler;
	@Mock protected EIDPKIRAServiceClient serviceClient;
	@Mock protected EIDPKIRAContractsClient contractsClient;
	@Mock protected HttpServletRequest request;
	@Mock protected HttpServletResponse response;
	@Mock protected FacesMessages facesMessages;
	protected T certificateResponse;
	
	@BeforeMethod
	protected void setUp() throws Exception {
		initMocks();
		initHandler();
	}

	protected abstract void initHandler();

	protected void initMocks() throws Exception {
		MockitoAnnotations.initMocks(this);
		when(request.getContextPath()).thenReturn("");
		when(request.getParameter(SIGNATURE_RESPONSE_PARAMETER)).thenReturn("");
	}
}
