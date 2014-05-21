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
