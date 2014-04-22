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
package be.fedict.eid.pkira.common.security;

import javax.servlet.http.HttpServletRequest;

import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class AuthenticationHandlerBeanTest {

	private static class TestAuthenticationHandler extends AbstractAuthenticationHandlerBean {
		@Override
		protected void enrichIdentity(EIdUser eidUser) {}

		@Override
		protected String getIDPDestination() {
			return "IDPDestination";
		}

		@Override
		protected String getSPDestination() {
			return "SPDestination";
		}

		@Override
		protected String getSPReturnUrl() {
			return "SPReturnURL";
		}
	}
	
	private AbstractAuthenticationHandlerBean handler;

	private EIdUserCredentials credentials;
	private AuthenticationRequestDecoder authenticationRequestDecoder;
	private Log log;

	@BeforeMethod
	public void setup() {		
		credentials = new EIdUserCredentials();
		authenticationRequestDecoder = mock(AuthenticationRequestDecoder.class);
		log = Logging.getLog(AbstractAuthenticationHandlerBean.class);

		handler = spy(new TestAuthenticationHandler());
		handler.setCredentials(credentials);
		handler.setAuthenticationRequestDecoder(authenticationRequestDecoder);
		handler.setLog(log);
	}

	@Test
	public void testAuthenticate() throws Exception {
		EIdUser eidUser = new EIdUser("A", "B", "C");
		HttpServletRequest request = mock(HttpServletRequest.class);
		
		doReturn(request).when(handler).getRequest();		
		when(authenticationRequestDecoder.decode(request)).thenReturn(eidUser);

		handler.authenticate();
		
		assertEquals(credentials.getUser(), eidUser);
		assertTrue(credentials.isInitialized());
		verify(handler).enrichIdentity(eq(eidUser));
	}
}
