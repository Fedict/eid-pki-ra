package be.fedict.eid.pkira.portal.security;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.common.security.AbstractPkiRaAuthenticationResponseService;
import be.fedict.eid.pkira.portal.util.ConfigurationEntryContainer;

@Name(PkiRaAuthenticationResponseService.NAME)
public class PkiRaAuthenticationResponseService extends AbstractPkiRaAuthenticationResponseService {

	@In(value = ConfigurationEntryContainer.NAME, create=true)
	protected ConfigurationEntryContainer configurationEntryContainer;
	
	@Override
	public String[] getFingerprints() {
		return configurationEntryContainer.getIdpFingerprints();
	}

	@Override
	public int getMaximumTimeOffset() {
		return configurationEntryContainer.getMaxTimeOffset();
	}

}
