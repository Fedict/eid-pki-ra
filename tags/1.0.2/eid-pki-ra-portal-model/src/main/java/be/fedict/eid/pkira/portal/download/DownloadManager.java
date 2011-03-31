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

package be.fedict.eid.pkira.portal.download;

import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;


/**
 * @author Bram Baeyens
 *
 */
@Name(DownloadManager.NAME)
@Scope(ScopeType.STATELESS)
public class DownloadManager implements Serializable {

	private static final long serialVersionUID = -3602220524378795120L;
	
	public static final String NAME = "be.fedict.eid.pkira.portal.downloadManager";
	
	@In(value="#{facesContext}")
	private FacesContext facesContext;
	
	@Logger
	private Log log;
	
	public void download(Document document) {
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		
		response.setContentType(document.getContentType());
		response.addHeader("Content-disposition", constructAttachmentHeader(document.getFileName()));
		
		try{
			ServletOutputStream servletOutputStream = response.getOutputStream();
			servletOutputStream.write(document.getData());
			servletOutputStream.flush();
			servletOutputStream.close();
			facesContext.responseComplete();
		}catch(Exception e){
			log.error("Failure: " + e.toString());
		}
	}

	private String constructAttachmentHeader(String fileName) {
		return new StringBuilder()
				.append("attachment; filename=\"")
				.append(fileName)
				.append("\"")
				.toString();
	}

}
