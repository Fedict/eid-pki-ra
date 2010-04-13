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
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.MapKeyManyToMany;

/**
 * Certificate authority entity.
 * 
 * @author Jan Van den Bergh
 */
@Entity
@Table(name = "CA")
public class CertificateAuthority implements Serializable {

	private static final long serialVersionUID = -7367938441889939933L;

	@Id
	@GeneratedValue
	@Column(name = "CA_ID", nullable = false, unique = true)
	private Integer id;

	@Column(name = "NAME", nullable = false, unique = true)
	private String name;

	@Column(name = "XKMS_URL", nullable = false, unique = true)
	private String xkmsUrl;

	@CollectionOfElements(targetElement = String.class)
	@MapKeyManyToMany(targetEntity = String.class)
	private Map<String, String> parameters;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getXkmsUrl() {
		return xkmsUrl;
	}

	public void setXkmsUrl(String xkmsUrl) {
		this.xkmsUrl = xkmsUrl;
	}

	public Map<String, String> getParameters() {
		if (parameters==null) {
			parameters = new HashMap<String, String>();
		}
		return parameters;
	}

	public Integer getId() {
		return id;
	}

}
