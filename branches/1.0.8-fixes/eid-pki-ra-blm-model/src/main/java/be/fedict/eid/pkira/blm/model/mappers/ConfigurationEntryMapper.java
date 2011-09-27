package be.fedict.eid.pkira.blm.model.mappers;

import be.fedict.eid.pkira.blm.model.config.ConfigurationEntry;
import be.fedict.eid.pkira.blm.model.config.ConfigurationEntryKey;
import be.fedict.eid.pkira.generated.privatews.ConfigurationEntryKeyWS;
import be.fedict.eid.pkira.generated.privatews.ConfigurationEntryWS;

public interface ConfigurationEntryMapper {
	
	String NAME = "be.fedict.eid.pkira.blm.configurationEntryMapper";
	
	ConfigurationEntryWS map(ConfigurationEntry configurationEntry);
	
	ConfigurationEntryKey map(ConfigurationEntryKeyWS configurationEntryKeyWS);
}
