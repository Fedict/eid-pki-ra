package be.fedict.eid.pkira.blm.model.config;

import java.util.Arrays;
import java.util.List;

import javax.persistence.NoResultException;

import org.jboss.seam.annotations.Name;

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
		return entry;
	}
	
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
	
	public ConfigurationEntry getConfigurationEntry() {
		return configurationEntry;
	}
}
