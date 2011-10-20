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

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.common.download.Document;
import be.fedict.eid.pkira.common.download.DownloadManager;
import be.fedict.eid.pkira.portal.framework.WSHome;

/**
 * @author Bram Baeyens
 * 
 */
@Name(ContractDocumentWSHome.NAME)
public class ContractDocumentWSHome extends WSHome<String> {

	private static final long serialVersionUID = -84641725603578997L;

	public static final String NAME = "be.fedict.eid.pkira.portal.contractDocumentWSHome";
	
	@In(value=DownloadManager.NAME, create=true)
	private DownloadManager downloadManager;
	
	
	@Override
	public String find() {
		return getServiceClient().findContractDocument((Integer) getId());
	}
	
	public String getContentType() {
		return "application/xml";
	}
	
	public String getFileName() {
		return "contract-".concat(getId().toString()).concat(".xml");
	}	
	
	public void download() {
		downloadManager.download(new Document(getFileName(), getContentType(), getInstance().getBytes()));			
	}
}
