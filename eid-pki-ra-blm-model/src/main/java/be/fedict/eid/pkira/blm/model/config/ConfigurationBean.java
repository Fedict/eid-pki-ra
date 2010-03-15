/*
 * eID PKI RA Project.
 * Copyright (C) 2010 FedICT.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version
 * 3.0 as published by the Free Software Foundation.
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

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.seam.annotations.Name;

/**
 * Implementation of the configuration.
 * 
 * @author Jan Van den Bergh
 */
@Stateless
@Name(Configuration.NAME)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ConfigurationBean implements Configuration {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getConfigurationValue(String key) {
		ConfigurationEntry entry = findConfigurationEntry(key);
		return entry != null ? entry.getValue() : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setConfigurationValue(String key, String value) {
		ConfigurationEntry entry = findConfigurationEntry(key);
		if (entry == null) {
			entry = new ConfigurationEntry();
			entry.setKey(key);
		}

		entry.setValue(value);
		entityManager.persist(entry);
	}

	private ConfigurationEntry findConfigurationEntry(String key) {
		Query query = entityManager.createQuery("FROM ConfigurationEntry e WHERE e.key=?");
		query.setParameter(1, key);

		try {
			return (ConfigurationEntry) query.getSingleResult();
		} catch (EntityNotFoundException e) {
			return null;
		} catch (NoResultException e) {
			return null;
		}
	}

	protected void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
