package be.fedict.eid.pkira.blm.model.framework;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Conversation;

import be.fedict.eid.dss.client.DigitalSignatureServiceClient;
import be.fedict.eid.pkira.blm.model.config.ConfigurationEntryKey;
import be.fedict.eid.pkira.blm.model.config.ConfigurationEntryQuery;

@Name(WebserviceLocator.NAME)
@Scope(ScopeType.STATELESS)
public class WebserviceLocator {
	
	public static final String NAME = "be.fedict.eid.pkira.blm.webserviceLocator";
	
	@In(value = ConfigurationEntryQuery.NAME, create = true) 
	private ConfigurationEntryQuery configurationEntryQuery;
	
	public DigitalSignatureServiceClient getDigitalSignatureServiceClient() {
		return new DigitalSignatureServiceClient(findWebsericeUrl(ConfigurationEntryKey.DSS_WS_CLIENT));
	}
	
	public String getAuthenticationLoginURL() {
		try {
			StringBuilder builder = new StringBuilder(128)
					.append(findWebsericeUrl(ConfigurationEntryKey.IDP_SERVLET))
					.append("?IdPDestination=")
					.append(findWebsericeUrl(ConfigurationEntryKey.IDP_DESTINATION));			

			String returnURL = getRequest().getRequestURL().toString();
			returnURL = returnURL.replaceFirst("/[^/]*$", "/postLogin.seam");
			returnURL += "?cid=" + Conversation.instance().getId();
			String parameter = "SPDestination=" + URLEncoder.encode(returnURL, "UTF-8");

			if (builder.indexOf("?") != -1) {
				return builder.append('&').append(parameter).toString();
			} else {
				return builder.append('?').append(parameter).toString();
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	protected HttpServletRequest getRequest() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		return request;
	}
	
	private String findWebsericeUrl(ConfigurationEntryKey configurationEntryKey) {
		return configurationEntryQuery.findByEntryKey(configurationEntryKey).getValue();
	}
}
