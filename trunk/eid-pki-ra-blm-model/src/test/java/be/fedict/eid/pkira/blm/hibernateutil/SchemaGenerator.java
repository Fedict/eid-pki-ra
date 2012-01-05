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
package be.fedict.eid.pkira.blm.hibernateutil;

import java.io.File;
import java.net.URL;
import java.util.Set;

import javax.persistence.Entity;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.scannotation.AnnotationDB;

/**
 * Hibernate Schema Generator Code based on
 * http://jandrewthompson.blogspot.com/2009
 * /10/how-to-generate-ddl-scripts-from.html
 */
public class SchemaGenerator {

	/**
	 * Holds the classnames of hibernate dialects for easy reference.
	 */
	private static enum Dialect {
		HSQL("org.hibernate.dialect.HSQLDialect"), 
		MYSQL("org.hibernate.dialect.MySQLDialect"), 
		ORACLE("org.hibernate.dialect.Oracle10gDialect"), 
		POSTGRES("org.hibernate.dialect.PostgreSQLDialect");

		private String dialectClass;

		private Dialect(String dialectClass) {
			this.dialectClass = dialectClass;
		}

		public String getDialectClass() {
			return dialectClass;
		}
	}

	private static final String PACKAGE_NAME = "be.fedict.eid.pkira";

	public static void main(String[] args) throws Exception {
		String workingDir;
		if (args.length > 0) {
			workingDir = new File(args[0]).getCanonicalPath();
		} else {
			workingDir = new File("target").getCanonicalPath();
		}

		SchemaGenerator gen = new SchemaGenerator("be.fedict.eid.pkira.blm.model", workingDir);
		gen.generate(Dialect.MYSQL);
		gen.generate(Dialect.ORACLE);
		gen.generate(Dialect.HSQL);
		gen.generate(Dialect.POSTGRES);
	}

	private final AnnotationConfiguration cfg;
	private final String workingDir;

	public SchemaGenerator(String packageName, String workingDir) throws Exception {
		this.workingDir = workingDir;
		
		// Find entities on the class path
		URL[] urls = new URL[1];
		urls[0] = new File(workingDir + "/classes").getCanonicalFile().toURI().toURL();
		AnnotationDB db = new AnnotationDB();
		db.scanArchives(urls);
		System.out.println(db.getAnnotationIndex().keySet());
		Set<String> classNames = db.getAnnotationIndex().get(Entity.class.getName());
		if (classNames == null || classNames.size() == 0) {
			throw new Exception("No entity classes found in classes directory: " + urls[0]);
		}
		System.out.println("Found " + classNames.size() + " entity classes.");

		// Create hibernate configuration
		cfg = new AnnotationConfiguration();
		cfg.setProperty("hibernate.hbm2ddl.auto", "create");
		for (String className : classNames) {
			if (className.startsWith(PACKAGE_NAME)) {
				Class<?> clazz = Class.forName(className);
				cfg.addAnnotatedClass(clazz);
			}
		}
	}

	/**
	 * Method that actually creates the file.
	 * 
	 * @param dbDialect
	 *            to use
	 */
	private void generate(Dialect dialect) {
		new java.io.File(workingDir + "/schema").mkdirs();

		cfg.setProperty("hibernate.dialect", dialect.getDialectClass());

		SchemaExport export = new SchemaExport(cfg);
		export.setDelimiter(";");
		export.setOutputFile(workingDir + "/schema/ddl_" + dialect.name().toLowerCase() + ".sql");
		export.execute(true, false, false, false);
	}
}
