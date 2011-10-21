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

package be.fedict.eid.pkira.blm.model.xkmslog;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.blm.model.certificatedomain.validation.UniqueCertificateDomain;
import be.fedict.eid.pkira.xkmsws.XKMSLogger.XKMSMessageType;

/**
 * @author Bram Baeyens
 */
@Entity
@Table(name = "XKMS_LOG")
@Name(XKMSLogEntry.NAME)
@UniqueCertificateDomain
public class XKMSLogEntry implements Serializable {

	private static final long serialVersionUID = -4193917177011312256L;

	public static final String NAME = "be.fedict.eid.pkira.blm.XKMSLogEntry";

	@Id
	@GeneratedValue
	@Column(name = "XKMS_LOG_ID")
	private Integer id;

	@Column(name = "MSGTYPE", nullable = false)
	@Enumerated(EnumType.STRING)
	private XKMSMessageType messageType;
	
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	@Column(name = "CREATION_DATE", nullable = false)
	private Date creationDate;

	@Column(name = "REQUEST_MSG", nullable = true)
	@Basic(fetch = FetchType.LAZY)
	@Lob
	private String requestMessage;

	@Column(name = "RESPONSE_MSG", nullable = true)
	@Basic(fetch = FetchType.LAZY)
	@Lob
	private String responseMessage;

	@Column(name = "STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private XKMSStatus status;

	@Column(name = "ERROR_MSG", nullable = true)
	@Lob
	private String errorMessage;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	public XKMSMessageType getMessageType() {
		return messageType;
	}

	
	public void setMessageType(XKMSMessageType messageType) {
		this.messageType = messageType;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getRequestMessage() {
		return requestMessage;
	}

	public void setRequestMessage(String requestMessage) {
		this.requestMessage = requestMessage;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public XKMSStatus getStatus() {
		return status;
	}

	public void setStatus(XKMSStatus status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof XKMSLogEntry)) {
			return false;
		}
		XKMSLogEntry that = (XKMSLogEntry) obj;
		return this.id == null ? false : this.id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 43).append(id).toHashCode();
	}

}
