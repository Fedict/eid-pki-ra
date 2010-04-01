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

package be.fedict.eid.pkira.portal.ra.certificaterequest;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.pkira.crypto.CSRInfo;
import be.fedict.eid.pkira.crypto.CSRParser;
import be.fedict.eid.pkira.crypto.CryptoException;

/**
 * @author Bram Baeyens
 *
 */
@Name(CSRUpload.NAME)
@Scope(ScopeType.EVENT)
public class CSRUpload implements Serializable {

	private static final long serialVersionUID = -3612151005409550321L;
	
	public static final String NAME = "be.fedict.eid.pkira.portal.csrUpload";
	
	private byte[] csrAsBytes;
	private String contentType;
	private String csrAsString;
	private CSRParser csrParser;
	
	@In(create=true, value=CSRParser.NAME)
	protected void setCsrParser(CSRParser csrParser) {
		this.csrParser = csrParser;
	}

	public byte[] getCsrAsBytes() {
		return csrAsBytes;
	}
	
	public void setCsrAsBytes(byte[] csrAsBytes) {
		this.csrAsBytes = csrAsBytes;
	}
	
	public String getContentType() {
		return contentType;
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public String getCsrAsString() {
		return csrAsString;
	}

	public void setCsrAsString(String csrAsString) {
		this.csrAsString = csrAsString;
	}
	
	public String getBase64Csr() {
		if (csrAsBytes != null && csrAsBytes.length > 0) {
			return new String(csrAsBytes);
		} else {
			return csrAsString;
		}
	}
	
	public CSRInfo extractCsrInfo() throws CryptoException {
		return csrParser.parseCSR(getBase64Csr());
	}
	
	@Override
	public String toString() {
		return new StringBuilder("CsrUpload[")
			.append("base64Csr=").append(getBase64Csr())
			.append(']').toString();
	}
}
