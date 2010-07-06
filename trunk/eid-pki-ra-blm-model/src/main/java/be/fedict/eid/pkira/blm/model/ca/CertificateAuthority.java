/*
 * eID PKI RA Project.
 * Copyright (C) 2010 FedICT.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version
 * 3.0 as published by the Free Software Foundation.
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, see
 * http://www.gnu.org/licenses/.
 */
package be.fedict.eid.pkira.blm.model.ca;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.validator.NotEmpty;
import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.blm.model.certificatedomain.validation.UniqueCertificateAuthorityName;

/**
 * Certificate authority entity.
 * 
 * @author Jan Van den Bergh
 */
@Entity
@NamedQueries(
		{
				@NamedQuery(name = "findCertificateAuthorityByName", query = "FROM CertificateAuthority WHERE name = :name")
		})
@Table(name = "CA")
@Name(CertificateAuthority.NAME)
public class CertificateAuthority implements Serializable {

	private static final long serialVersionUID = -7367938441889939933L;

	public static final String NAME="be.fedict.eid.pkira.blm.certificateAuthority";
	
	@JoinColumn(name="CERTIFICATE_CHAIN_ID")
	@OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.REMOVE)
	private CertificateChain certificateChain;

	@Id
	@GeneratedValue
	@Column(name = "CA_ID", nullable = false, unique = true)
	private Integer id;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "LEGAL_NOTICE", nullable = false)
	private String legalNotice;

	@Column(name = "NAME", nullable = false, unique = true)
	//@NotEmpty(message = "{validation.empty.certificateauthorityname}")
	//@UniqueCertificateAuthorityName
	private String name;

	@OneToMany(cascade=CascadeType.ALL ,fetch=FetchType.LAZY, mappedBy="ca")
	private List<CertificateAuthorityParameter> parameters;
	
	@Column(name = "XKMS_URL", nullable = false)
	private String xkmsUrl;
	
	public CertificateChain getCertificateChain() {
		return certificateChain;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getXkmsUrl() {
		return xkmsUrl;
	}
	
	public void setXkmsUrl(String xkmsUrl){
		this.xkmsUrl = xkmsUrl;
	}

	public void setCertificateChain(CertificateChain certificateChain) {
		this.certificateChain = certificateChain;
	}

	public void setLegalNotice(String legalNotice) {
		this.legalNotice = legalNotice;
	}

	public String getLegalNotice() {
		return legalNotice;
	}

	public void setParameters(List<CertificateAuthorityParameter> parameters) {
		this.parameters = parameters;
	}

	public List<CertificateAuthorityParameter> getParameters() {
		if (parameters == null) {
			parameters = new ArrayList<CertificateAuthorityParameter>();
		}
		return parameters;
	}
	
	public Map<String, String> getParametersAsMap(){
		Map<String, String> map = new HashMap<String, String>();
		for(CertificateAuthorityParameter parameter : getParameters()){
			map.put(parameter.getKey(), parameter.getValue());
		}
		return map;
	}
	
	public String getCertificateAuthorityParameterValueForKey(String key){
		for(CertificateAuthorityParameter parameter: getParameters()){
			if(key.equals(parameter.getKey())){
				return parameter.getValue();
			}
		}
		return "";
	} 
}

