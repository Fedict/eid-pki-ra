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
package be.fedict.eid.pkira.blm.errorhandling;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

/**
 * Implementation of the error logger.
 * 
 * @author Jan Van den Bergh
 */
@Stateless
@Name(ErrorLogger.NAME)
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class ErrorLoggerBean implements ErrorLogger {

	public static final String CATEGORY = "ErrorLog";
	
	@Logger(value = CATEGORY)
	private Log log;

	@Override
	public void logError(Component component, String message, Throwable exception) {
		log.error("[{0}] {1}", exception, component, message);
	}

	@Override
	public void logError(Component component, String message) {
		log.error("[{0}] {1}", component, message);
	}

	protected void setLog(Log log) {
		this.log = log;
	}
}
