package be.fedict.eid.pkira.blm.model.config;

import java.util.Arrays;
import java.util.List;

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
	
	public List<ConfigurationEntry> getResultList() {
		setOrderColumn("configurationEntry.key");
		return super.getResultList();
	}
	
	public ConfigurationEntry findByEntryKey(ConfigurationEntryKey configurationEntryKey) {
		configurationEntry = new ConfigurationEntry();
		configurationEntry.setKey(configurationEntryKey);
		setRestrictionExpressionStrings(Arrays.asList(
				new String[] {
						"configurationEntry.key = #{configurationEntryQuery.configurationEntry.key}"}));
		return getSingleResult();
	}
	
	public ConfigurationEntry getConfigurationEntry() {
		return configurationEntry;
	}
}
