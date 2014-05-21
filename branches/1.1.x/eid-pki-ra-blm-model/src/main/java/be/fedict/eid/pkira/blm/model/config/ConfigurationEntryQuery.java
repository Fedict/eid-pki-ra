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

package be.fedict.eid.pkira.blm.model.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;

import be.fedict.eid.pkira.blm.model.framework.DataTableEntityQuery;

@Name(ConfigurationEntryQuery.NAME)
public class ConfigurationEntryQuery extends DataTableEntityQuery<ConfigurationEntry> {

	private static final long serialVersionUID = -7625408832624979621L;
	
	public static final String NAME = "be.fedict.eid.pkira.blm.configurationEntryQuery";
	
	private ConfigurationEntry configurationEntry;
	
	@Override
	public String getEjbql() {
		return "select configurationEntry from ConfigurationEntry configurationEntry";
	}
	
	@Override
	public List<ConfigurationEntry> getResultList() {
		// Get the current entries
		setOrderColumn("configurationEntry.key");
		List<ConfigurationEntry> entries = super.getResultList();
		
		// Add the missing entries
		nextKey: for(ConfigurationEntryKey key: ConfigurationEntryKey.values()) {
			for(ConfigurationEntry entry: entries) {
				if (key.equals(entry.getKey())) {
					continue nextKey;
				}
			}
			entries.add(addEntry(key));
		}
		
		return entries;
	}

	private ConfigurationEntry addEntry(ConfigurationEntryKey key) {
		ConfigurationEntry entry = new ConfigurationEntry();
		entry.setKey(key);
		entry.setValue(key.getDefaultValue());
		getEntityManager().persist(entry);
		getEntityManager().flush();
		getEntityManager().clear();
		return entry;
	}
	
	@Transactional(TransactionPropagationType.REQUIRED)
	public ConfigurationEntry findByEntryKey(ConfigurationEntryKey configurationEntryKey) {
		configurationEntry = new ConfigurationEntry();
		configurationEntry.setKey(configurationEntryKey);
		setRestrictionExpressionStrings(Arrays.asList(
				new String[] {
						"configurationEntry.key = #{configurationEntryQuery.configurationEntry.key}"}));
		
		ConfigurationEntry result = null;
		try {
			result = getSingleResult();
		} catch (NoResultException e) {
		}
		if (result==null) {
			return addEntry(configurationEntryKey);
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, String> getAsMap() {
		List<Object[]> allValues = this.getEntityManager().createNamedQuery(ConfigurationEntry.NQ_SELECT_ALL).getResultList();
		
		Map<String, String> result = new HashMap<String, String>();
		for(Object[] value: allValues) {
			result.put(((ConfigurationEntryKey) value[0]).name(), (String) value[1]);
		}
		
		for(ConfigurationEntryKey key: ConfigurationEntryKey.values()) {
			String name = key.name();
			if (!result.containsKey(name)) {
				result.put(name, key.getDefaultValue());
			}
		}
		
		return result;
	}
	
	public ConfigurationEntry getConfigurationEntry() {
		return configurationEntry;
	}
}
