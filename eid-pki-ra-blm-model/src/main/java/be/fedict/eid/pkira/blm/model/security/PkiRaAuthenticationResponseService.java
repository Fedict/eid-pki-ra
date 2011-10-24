package be.fedict.eid.pkira.blm.model.security;

import org.apache.commons.lang.StringUtils;
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
	public String[] getFingerprints() {
		String fingerprint1 = configurationEntryQuery.findByEntryKey(ConfigurationEntryKey.IDP_FINGERPRINT).getValue();
		String fingerprint2 = configurationEntryQuery.findByEntryKey(ConfigurationEntryKey.IDP_FINGERPRINT_ROLLOVER)
				.getValue();

		if (StringUtils.isBlank(fingerprint1) && StringUtils.isBlank(fingerprint2)) {
			return new String[0];
		}
		
		if (StringUtils.isBlank(fingerprint1)) {
			return new String[]
				{ fingerprint2 };
		}
		if (StringUtils.isBlank(fingerprint2)) {
			return new String[]
				{ fingerprint1 };
		}
		return new String[]
			{ fingerprint1, fingerprint2 };
	}

	@Override
	public int getMaximumTimeOffset() {
		String value = configurationEntryQuery.findByEntryKey(ConfigurationEntryKey.IDP_MAXTIMEOFFSET).getValue();
		if (value == null || value.isEmpty()) {
			return 0;
		}
		return Integer.parseInt(value);
	}

}
