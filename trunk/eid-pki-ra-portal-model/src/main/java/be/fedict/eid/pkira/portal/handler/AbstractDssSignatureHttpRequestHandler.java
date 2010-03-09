package be.fedict.eid.pkira.portal.handler;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.jboss.seam.Component;
import org.jboss.seam.servlet.ContextualHttpServletRequest;
import org.jboss.seam.web.AbstractResource;

import be.fedict.eid.pkira.contracts.EIDPKIRAContractsClient;
import be.fedict.eid.pkira.contracts.XmlMarshallingException;
import be.fedict.eid.pkira.generated.contracts.ResponseType;
import be.fedict.eid.pkira.generated.contracts.ResultType;
import be.fedict.eid.pkira.publicws.EIDPKIRAServiceClient;

public abstract class AbstractDssSignatureHttpRequestHandler<T extends ResponseType> extends AbstractResource {

	private static final Logger LOG = Logger.getLogger("AbstractDssSignatureHttpRequestHandler");

	private static final String SIGNATURE_RESPONSE_PARAMETER = "SignatureResponse";
	private static final String SIGNATURE_STATUS_PARAMETER = "SignatureStatus";
	
	private static final String SUCCESSFUL_REDIRECT = "success";

	@Override
	public void getResource(final HttpServletRequest request, final HttpServletResponse response) 
			throws ServletException, IOException {

		new ContextualHttpServletRequest(request) {
			@Override
			public void process() throws IOException {
				handleRequest(request, response);
			}
		}.run();
	}

	protected void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		LOG.info(">>> handleRequest()");
		String redirectStatus = null;
		try {
			String signatureStatus = nullSafeGetRequestParameter(request, SIGNATURE_STATUS_PARAMETER);
			if ("OK".equals(signatureStatus)) {				
				String signatureResponse = nullSafeGetRequestParameter(request, SIGNATURE_RESPONSE_PARAMETER);
				byte[] contract = Base64.decodeBase64(signatureResponse);
				String result = invokeServiceClient(new String(contract));
				T serviceClientResponse = unmarshall(result);
				if (ResultType.SUCCESS.equals(serviceClientResponse.getResult())) {
					// TODO (20100308): put necessary data on the context
					redirectStatus = SUCCESSFUL_REDIRECT;
				} 
			}
		} catch (Exception e) {
			// TODO (20100308): add proper exception handling
			LOG.info("<<< handleRequest: exception");
		}
		handleRedirect(request, response, redirectStatus);
		LOG.info("<<< handleRequest: redirectStatus[" + redirectStatus + ']');
	}

	protected void handleRedirect(HttpServletRequest request, HttpServletResponse response, String redirectStatus) throws IOException {
		if (SUCCESSFUL_REDIRECT.equals(redirectStatus)) {
			response.sendRedirect(getNextPage(request));
		} else {
			response.sendRedirect(getErrorPage(request));
		}		
	}

	protected abstract String getErrorPage();

	protected abstract String getNextPage();

	protected abstract String invokeServiceClient(String signedRequest)throws Exception;

	protected abstract T unmarshall(String result)throws XmlMarshallingException;

	protected String nullSafeGetRequestParameter(HttpServletRequest request, String parameterName)
			throws ServletException {
		LOG.info(">>> nullSafeGetRequestParameter(parameterName[" + parameterName + "])");
		String parameter = request.getParameter(parameterName);
		if (parameter == null) {
			throw new ServletException(parameterName + " parameter not present");
		}
		LOG.info("<<< nullSafeGetRequestParameter: " + parameter);
		return parameter;
	}

	private String getErrorPage(HttpServletRequest request) {
		return request.getContextPath().concat(getErrorPage());
	}

	private String getNextPage(HttpServletRequest request) {
		return request.getContextPath().concat(getNextPage());
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
}
