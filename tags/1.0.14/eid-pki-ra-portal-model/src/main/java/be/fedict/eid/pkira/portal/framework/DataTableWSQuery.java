/**
 * eID PKI RA Project. 
 * Copyright (C) 2010-2012 FedICT. 
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

package be.fedict.eid.pkira.portal.framework;

/**
 * @author Bram Baeyens
 *
 */
public abstract class DataTableWSQuery extends PrivateWSQuery {
	
	private static final long serialVersionUID = 5506541459954523480L;
	
	private static final int DEFAULT_ROWS = 20;
	private static final int DEFAULT_MAX_PAGES = 10;
	private static final int DEFAULT_FAST_STEP = 5;
	
	public int getRows() {
		return DEFAULT_ROWS;
	}

	public int getMaxPages() {
		return DEFAULT_MAX_PAGES;
	}

	public int getFastStep() {
		return DEFAULT_FAST_STEP;
	}
}
