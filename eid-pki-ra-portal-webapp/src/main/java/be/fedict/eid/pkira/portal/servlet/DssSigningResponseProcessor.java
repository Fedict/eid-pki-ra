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

package be.fedict.eid.pkira.portal.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.jboss.seam.Component;
import org.jboss.seam.servlet.ContextualHttpServletRequest;

import be.fedict.eid.pkira.portal.csr.CertificateHandler;

/**
 * @author Bram Baeyens
 *
 */
public class DssSigningResponseProcessor  extends HttpServlet {

	private static final long serialVersionUID = 2530434708654900535L;
	
	private static final Logger LOG = Logger.getLogger("DssSigningResponseProcessor");
	
	public static final String SIGNATURE_RESPONSE_PARAMETER = "SignatureResponse";
	public static final String SIGNATURE_STATUS_PARAMETER = "SignatureStatus";
	public static final String NEXT_PAGE_INIT_PARAM = "nextPage";
	public static final String ERROR_PAGE_INIT_PARAM = "errorPage";

	private String nextPage;
	private String errorPage;

	@Override
	public void init(ServletConfig config) throws ServletException {
		LOG.info(">>> init()");
		this.nextPage = nullSafeGetConfigParameter(config, NEXT_PAGE_INIT_PARAM);
		this.errorPage = nullSafeGetConfigParameter(config, ERROR_PAGE_INIT_PARAM);
		LOG.info("<<< init");
	}

	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		LOG.info(">>> doPost()");
		new ContextualHttpServletRequest(request) {
            @Override
            public void process() throws Exception {
                doWork(request, response);
            }
        }.run();
	}	
		
	protected void doWork(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String signatureStatus = nullSafeGetRequestParameter(request, SIGNATURE_STATUS_PARAMETER);
		if (!"OK".equals(signatureStatus)) {
			response.sendRedirect(request.getContextPath().concat(this.errorPage));
			LOG.info("<<< doPost");
			return;
		}
		String signatureResponse = nullSafeGetRequestParameter(request, SIGNATURE_RESPONSE_PARAMETER);
		byte[] decodedSignatureResponse = Base64.decodeBase64(signatureResponse);
		LOG.info("decoded: " + new String(decodedSignatureResponse));
		CertificateHandler certificateHandler = (CertificateHandler) Component.getInstance("certificateHandler");
		String result = certificateHandler.requestCertificateSigningRequest(new String(decodedSignatureResponse));
		if ("success".equals(result)) {
			LOG.info("<<< doPost");		
			response.sendRedirect(request.getContextPath().concat(this.nextPage));
		} else {
			LOG.info("<<< doPost");		
			response.sendRedirect(request.getContextPath().concat(this.errorPage));
		}		
	}

	private static String nullSafeGetConfigParameter(ServletConfig config, String parameterName) throws ServletException {
		LOG.info(">>> nullSafeGetConfigParameter(parameterName[" + parameterName + "])");
		String parameter = config.getInitParameter(parameterName);
		if (parameter == null) {
			throw new ServletException("missing init-param: " + parameterName);
		}
		LOG.info("<<< nullSafeGetConfigParameter: " + parameter);
		return parameter;
	}

	private static String nullSafeGetRequestParameter(HttpServletRequest request, String parameterName) throws ServletException {
		LOG.info(">>> nullSafeGetRequestParameter(parameterName[" + parameterName + "])");
		String parameter = request.getParameter(parameterName);
		if (parameter == null) {
			throw new ServletException(parameterName + " parameter not present");
		}
		LOG.info("<<< nullSafeGetRequestParameter: " + parameter);
		return parameter;
	}
}
