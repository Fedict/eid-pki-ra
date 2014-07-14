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

package be.fedict.eid.pkira.portal.certificate;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.common.download.Document;
import be.fedict.eid.pkira.common.download.DownloadManager;
import be.fedict.eid.pkira.portal.framework.WSHome;
import be.fedict.eid.pkira.portal.util.TypeMapper;

/**
 * @author Bram Baeyens
 *
 */
@Name(CertificateWSHome.NAME)
public class CertificateWSHome extends WSHome<Certificate> {

	private static final long serialVersionUID = -8168221884695474692L;
	
	public static final String NAME = "be.fedict.eid.pkira.portal.certificateWSHome";
	
	@In(value= TypeMapper.NAME, create=true)
	private TypeMapper typeMapper;
	
	@In(value=DownloadManager.NAME, create=true)
	private DownloadManager downloadManager;

	@Override
	public Certificate find() {
		return typeMapper.map(getServiceClient().findCertificate((Integer) getId()));
	}
	
	public void download(){
		downloadManager.download(new Document(
				getInstance().getSerialNumber() + ".zip", 
				"application/zip", 
				getInstance().getZippedCertificates()));
	}

}
