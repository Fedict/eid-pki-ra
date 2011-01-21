/**
 * eID PKI RA Project. 
 * Copyright (C) 2010 FedICT. 
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

package be.fedict.eid.pkira.common.security;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import be.fedict.eid.idp.sp.protocol.saml2.AuthenticationResponseProcessor;
import be.fedict.eid.idp.sp.protocol.saml2.AuthenticationResponseProcessorException;
import be.fedict.eid.idp.sp.protocol.saml2.spi.AuthenticationResponse;

/**
 * @author Bram Baeyens
 */
@Name(AuthenticationRequestDecoder.NAME)
public class Saml2RequestDecoder implements AuthenticationRequestDecoder {

	private static final String URN_BE_FEDICT_EID_IDP_FIRST_NAME = "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/givenname";
	private static final String URN_BE_FEDICT_EID_IDP_NAME = "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/surname";
	
	@Logger
	private static Log log;
	
	@In(value="be.fedict.eid.pkira.common.authenticationResponseProcessor")
	private AuthenticationResponseProcessor authenticationResponseProcessor;

	@Override
	public EIdUser decode(HttpServletRequest saml2Request) throws AuthenticationException {
		log.debug(">>> decode(saml2Request[{0}])", saml2Request);
		try {
			AuthenticationResponse authenticationResponse = this.authenticationResponseProcessor.process(null,
					saml2Request);

			Map<String, Object> attributeMap = authenticationResponse.getAttributeMap();
			return new EIdUser(authenticationResponse.getIdentifier(),
					(String) attributeMap.get(URN_BE_FEDICT_EID_IDP_FIRST_NAME),
					(String) attributeMap.get(URN_BE_FEDICT_EID_IDP_NAME));
		} catch (AuthenticationResponseProcessorException e) {
			log.error("Error decoding SAML request.", e);
			throw new AuthenticationException("Cannot decode SAML response", e);
		}
	}

	public void setAuthenticationResponseProcessor(AuthenticationResponseProcessor authenticationResponseProcessor) {
		this.authenticationResponseProcessor = authenticationResponseProcessor;
	}
}
