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

import java.net.URL;
import java.util.Set;

import javax.persistence.Entity;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.scannotation.AnnotationDB;
import org.scannotation.ClasspathUrlFinder;

/**
 * Hibernate Schema Generator Code based on
 * http://jandrewthompson.blogspot.com/2009/10/how-to-generate-ddl-scripts-from.html
 */
public class SchemaGenerator {

	/**
	 * Holds the classnames of hibernate dialects for easy reference.
	 */
	private static enum Dialect {
		HSQL("org.hibernate.dialect.HSQLDialect"), 
		MYSQL("org.hibernate.dialect.MySQLDialect"), 
		ORACLE("org.hibernate.dialect.Oracle10gDialect");

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
		SchemaGenerator gen = new SchemaGenerator("be.fedict.eid.pkira.blm.model");
		gen.generate(Dialect.MYSQL);
		gen.generate(Dialect.ORACLE);
		gen.generate(Dialect.HSQL);
	}

	private AnnotationConfiguration cfg;

	public SchemaGenerator(String packageName) throws Exception {
		// Find entities on the class path
		URL[] urls = ClasspathUrlFinder.findClassPaths();
		AnnotationDB db = new AnnotationDB();
		db.scanArchives(urls);
		Set<String> classNames = db.getAnnotationIndex().get(Entity.class.getName());

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
		cfg.setProperty("hibernate.dialect", dialect.getDialectClass());

		SchemaExport export = new SchemaExport(cfg);
		export.setDelimiter(";");
		export.setOutputFile("ddl_" + dialect.name().toLowerCase() + ".sql");
		export.execute(true, false, false, false);
	}
}
