package be.fedict.eid.pkira.blm.model.config;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name(ConfigurationEntryQuery.NAME)
public class ConfigurationEntryQuery extends EntityQuery<ConfigurationEntry> {

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
