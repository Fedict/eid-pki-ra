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

import javax.ejb.Local;

/**
 * Interface to the component to get the configuration.
 * 
 * @author Jan Van den Bergh
 */
@Local
public interface Configuration {

	static final String NAME = "configuration";
	
	/**
	 * Returns the value for a specific configuration key.
	 * @param key the key to the configuration value.
	 * @return the value.
	 */
	String getConfigurationValue(String key);
	
	/**
	 * Changes a configuration value.
	 * @param key key to change
	 * @param value the new value.
	 */
	void setConfigurationValue(String key, String value);
}
