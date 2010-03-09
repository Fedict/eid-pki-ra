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
package be.fedict.eid.pkira.blm.model.domain;

import java.util.Collections;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

/**
 * Base class for all tests using the database.
 * 
 * @author Jan Van den Bergh
 */
public class DatabaseTest {

	private static EntityManager entityManager;

	/**
	 * Sets up hibernate.
	 */
	@BeforeSuite
	public static void setupDatabase() {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("PersistenceUnitTest",
				Collections.emptyMap());
		entityManager = entityManagerFactory.createEntityManager();
	}

	private boolean commit;

	/**
	 * Forces a rollback of the transaction after the test.
	 * If this method is not called, the transaction will be committed.
	 */
	public final void forceCommit() {
		this.commit = true;
	}

	/**
	 * Returns the created entity manager.
	 */
	public static EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * Sets up transactions.
	 */
	@BeforeMethod
	public final void setupTransactions() {
		getEntityManager().getTransaction().begin();
		commit = false;
	}

	/**
	 * Shuts down transactions.
	 */
	@AfterMethod
	public final void tearDownTransactions() {
		EntityTransaction transaction = entityManager.getTransaction();
		if (commit) {
			transaction.commit();
		} else {
			transaction.rollback();
		}
	}
}
