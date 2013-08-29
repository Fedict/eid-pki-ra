package be.fedict.eid.pkira.blm.model.mappers;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.pkira.blm.model.config.ConfigurationEntry;
import be.fedict.eid.pkira.blm.model.config.ConfigurationEntryKey;
import be.fedict.eid.pkira.generated.privatews.ConfigurationEntryKeyWS;
import be.fedict.eid.pkira.generated.privatews.ConfigurationEntryWS;
import be.fedict.eid.pkira.generated.privatews.ObjectFactory;

@Name(ConfigurationEntryMapper.NAME)
@Scope(ScopeType.STATELESS)
public class ConfigurationEntryMapperBean implements ConfigurationEntryMapper {

	@Override
	public ConfigurationEntryWS map(ConfigurationEntry configurationEntry) {
		if (configurationEntry == null) {
			return null;
		}
		ConfigurationEntryWS configurationEntryWS = new ObjectFactory().createConfigurationEntryWS();
		configurationEntryWS.setEntryKey(
				Enum.valueOf(ConfigurationEntryKeyWS.class, configurationEntry.getKey().name()));
		configurationEntryWS.setEntryValue(configurationEntry.getValue());
		return configurationEntryWS;
	}

	@Override
	public ConfigurationEntryKey map(ConfigurationEntryKeyWS configurationEntryKeyWS) {
		if (configurationEntryKeyWS == null) {
			return null;
		}
		return Enum.valueOf(ConfigurationEntryKey.class, configurationEntryKeyWS.value());
	}

}
