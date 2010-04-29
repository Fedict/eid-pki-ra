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

package be.fedict.eid.pkira.portal.contract;

import javax.faces.context.ExternalContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import be.fedict.eid.pkira.portal.ra.WSHome;

/**
 * @author Bram Baeyens
 * 
 */
@Name(ContractDocumentWSHome.NAME)
public class ContractDocumentWSHome extends WSHome<String> {

	private static final long serialVersionUID = -84641725603578997L;

	public static final String NAME = "be.fedict.eid.pkira.portal.contractDocumentWSHome";
	
	@In(value="#{facesContext.externalContext}")
	private ExternalContext extCtx;
	
	@Logger
	private Log log;
	
	@In(value="#{facesContext}")
	private javax.faces.context.FacesContext facesContext;
	
	private String fileName;

	@Override
	public String find() {
		return getServiceClient().findContractDocument((Integer) getId());
	}
	
	public byte[] getData() {
		return getInstance().getBytes();
	}
	
	public String getContentType() {
		return "application/xml";
	}
	
	public String getFileName() {
		if (fileName == null) {
			fileName = "contract-".concat(getId().toString()).concat(".xml");
		}
		return fileName;
	}	
	
	public void download() {
		if (getId() != null) {
			HttpServletResponse response = (HttpServletResponse) extCtx.getResponse();

			response.setContentType(getContentType());
			response.addHeader("Content-disposition", "attachment; filename=\"" + getFileName() + "\"");
			
			try {
				ServletOutputStream servletOutputStream = response.getOutputStream();
				servletOutputStream.write(getData());
				servletOutputStream.flush();
				servletOutputStream.close();
				facesContext.responseComplete();
			} catch(Exception e){
				log.error("Failure: " + e.toString());
			}
		}
	}
}
