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

package be.fedict.eid.pkira.blm.model.xkmslog;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityHome;

import be.fedict.eid.pkira.common.download.Document;
import be.fedict.eid.pkira.common.download.DownloadManager;

/**
 * @author Bram Baeyens
 *
 */
@Name(XKMSLogEntryHome.NAME)
@Scope(ScopeType.CONVERSATION)
public class XKMSLogEntryHome extends EntityHome<XKMSLogEntry> {

	private static final long serialVersionUID = 4867309070918612476L;
	
	public static final String NAME = "be.fedict.eid.pkira.blm.xkmsLogEntryHome";

	@In(value=DownloadManager.NAME, create=true)
	private DownloadManager downloadManager;
	
	public void downloadRequest(){
		XKMSLogEntry instance = getInstance();
		downloadManager.download(new Document(
				"xkms-request-" + instance.getId() + ".xml",
				"text/xml", 
				instance.getRequestMessage().getBytes()));
	}
	
	public void downloadResponse(){
		XKMSLogEntry instance = getInstance();
		downloadManager.download(new Document(
				"xkms-response-" + instance.getId() + ".xml",
				"text/xml", 
				instance.getResponseMessage().getBytes()));
	}
}
