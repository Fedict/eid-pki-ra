package be.fedict.eid.pkira.blm.model.handler;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.NonUniqueObjectException;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.servlet.ContextualHttpServletRequest;
import org.jboss.seam.web.AbstractResource;

import be.fedict.eid.pkira.blm.model.domain.Registration;
import be.fedict.eid.pkira.blm.model.domain.User;
import be.fedict.eid.pkira.blm.model.jpa.UserRepository;

@Scope(ScopeType.APPLICATION)
@Name("simpleAuthenticationHandler")
@BypassInterceptors
public class SimpleAuthenticationHandler extends AbstractResource {
	
	private static final Logger LOG = Logger.getLogger("SimpleAuthenticationHandler");

	private static final String FIRST_NAME_PARAMETER = "FirstName";
	private static final String LAST_NAME_PARAMETER = "Name";
	
	private static final String SUCCESSFUL_REDIRECT = "success";
	
	@Out(value="newRegistration")
	private Registration registration;

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

	@Begin
	protected void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		LOG.info(">>> handleRequest()");
		String redirectStatus = null;
		try {
			User requester = (User) Component.getInstance("currentUser");
			requester.setFirstName(nullSafeGetRequestParameter(request, FIRST_NAME_PARAMETER));
			requester.setLastName(nullSafeGetRequestParameter(request, LAST_NAME_PARAMETER));	
			// TODO Bram: simple protocol doesn't return the national register number
			requester.setNationalRegisterNumber("123456789012");
			UserRepository userRepository = (UserRepository) Component.getInstance("userRepository", true);
			try {
				if (requester.getId() == null) {
					userRepository.persist(requester);
				}
			} catch (NonUniqueObjectException e) {
				LOG.info(requester + " already exists.");
			}
			registration = (Registration) Component.getInstance("newRegistration", true);
			registration.setRequester(requester);
			redirectStatus = SUCCESSFUL_REDIRECT;
		} catch (Exception e) {
			// TODO (20100308): add proper exception handling
			LOG.info("<<< handleRequest: exception: " + e.getMessage());
		}
		handleRedirect(request, response, redirectStatus);
		LOG.info("<<< handleRequest: redirectStatus[" + redirectStatus + ']');
	}

	protected void handleRedirect(HttpServletRequest request, HttpServletResponse response, String redirectStatus) throws IOException {
		if (SUCCESSFUL_REDIRECT.equals(redirectStatus)) {
			response.sendRedirect(request.getContextPath().concat("/page/registration/requestRegistration.seam"));
		} else {
			response.sendRedirect(request.getContextPath().concat("/home.seam"));
		}		
	}

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

	@Override
	public String getResourcePath() {
		return "/simpleAuthenticationHandler";
	}
}
