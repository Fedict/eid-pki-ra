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
package be.fedict.eid.pkira.blm.model;

import java.util.Collections;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.hsqldb.Server;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

/**
 * Base class for all tests using the database.
 * 
 * @author Jan Van den Bergh
 */
public class DatabaseTest {

	private static EntityManager entityManager;

	public static final Integer TEST_USER_ID = 2001;
	public static final Integer TEST_CERTIFICATE_DOMAIN_ID = 1003;

	/**
	 * Returns the created entity manager.
	 */
	public static EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * Sets up hibernate.
	 */
	@BeforeSuite
	public static void setupDatabase() {
		databaseServer = new Server();
		databaseServer.setDatabaseName(0, "test");
		databaseServer.setDatabasePath(0, "mem:test");
		databaseServer.setPort(9999);
		databaseServer.setLogWriter(null);
		databaseServer.setErrWriter(null);
		databaseServer.start();

		// Start hibernate
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("PersistenceUnitTest",
				Collections.emptyMap());
		entityManager = entityManagerFactory.createEntityManager();
	}

	@AfterSuite
	public static void stopDatabase() {
		databaseServer.stop();
	}

	private boolean commit;

	private static Server databaseServer;

	/**
	 * Forces a rollback of the transaction after the test. If this method is
	 * not called, the transaction will be committed.
	 */
	public final void forceCommit() {
		this.commit = true;
	}

	/**
	 * Persists an object using the entity manager.
	 */
	public final void persistObject(final Object object) {
		runInTransaction(new Runnable() {

			@Override
			public void run() {
				getEntityManager().persist(object);
			}
		});
	}

	public final <T> T loadObject(Class<T> clazz, Integer id) {
		return getEntityManager().find(clazz, id);
	}

	/**
	 * Runs an action in a transaction. If an exception occurs, the transaction
	 * is rolled back, otherwise committed.
	 */
	public final void runInTransaction(Runnable action) {
		EntityTransaction transaction = getEntityManager().getTransaction();
		transaction.begin();
		boolean ok = false;
		try {
			action.run();
			ok = true;
		} finally {
			if (ok) {
				transaction.commit();
			} else {
				transaction.rollback();
			}
		}
	}

	/**
	 * Merges the given objects back to the context.
	 */
	public final void merge(Object... entities) {
		for (Object entity : entities) {
			getEntityManager().merge(entity);
		}
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
