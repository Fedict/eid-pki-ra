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
package be.fedict.eid.pkira.blm.model.config;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import be.fedict.eid.pkira.blm.model.certificatedomain.validation.ValidConfigurationEntry;

/**
 * An entry in the configuration.
 * 
 * @author Jan Van den Bergh
 */
@Entity
@Table(name="CONFIGURATION")
@ValidConfigurationEntry
public class ConfigurationEntry {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ENTRY_ID")
	private Integer id;
	@Enumerated(EnumType.STRING)
	@Column(name="ENTRY_KEY", nullable=false, unique=true)
	private ConfigurationEntryKey key;
	@Column(name="ENTRY_VALUE", nullable=true)
	private String value;

	public ConfigurationEntryKey getKey() {
		return key;
	}
	
	public void setKey(ConfigurationEntryKey key) {
		this.key = key;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public Integer getId() {
		return id;
	}

	public boolean isValid() {
		try {
			return key.getConfigurationEntryType().isValid(value);
		} catch (NullPointerException e) {
			return false;
		}
	}
}
