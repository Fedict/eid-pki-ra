package be.fedict.eid.pkira.blm.model.security;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.blm.model.config.ConfigurationEntryKey;
import be.fedict.eid.pkira.blm.model.config.ConfigurationEntryQuery;
import be.fedict.eid.pkira.common.security.AbstractPkiRaAuthenticationResponseService;

@Name(PkiRaAuthenticationResponseService.NAME)
public class PkiRaAuthenticationResponseService extends AbstractPkiRaAuthenticationResponseService {

	@In(value = ConfigurationEntryQuery.NAME, create = true)
	private ConfigurationEntryQuery configurationEntryQuery;
	
	@Override
	public String getFingerprint() {
		return configurationEntryQuery.findByEntryKey(ConfigurationEntryKey.IDP_FINGERPRINT).getValue();
	}

	@Override
	public int getMaximumTimeOffset() {
		String value = configurationEntryQuery.findByEntryKey(ConfigurationEntryKey.IDP_MAXTIMEOFFSET).getValue();
		if(value == null || value.isEmpty()){
			return 0;
		}
		return Integer.parseInt(value);
	}

}
