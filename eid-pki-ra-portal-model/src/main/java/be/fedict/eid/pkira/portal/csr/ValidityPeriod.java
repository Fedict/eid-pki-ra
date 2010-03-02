/**
 * eID PKI RA Project. 
 * Copyright (C) 2010 FedICT. 
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

package be.fedict.eid.pkira.portal.csr;

/**
 * @author Bram.Baeyens
 *
 */
public enum ValidityPeriod {
	
	MONTH(1), THREE_MONTHS(3), SIX_MONTHS(6), ONE_YEAR(12), YEAR_AND_A_HALF(15), THREE_YEARS(36);
	
	private final int monthsInPeriod;
	
	private ValidityPeriod(int monthsInPeriod) {
		this.monthsInPeriod = monthsInPeriod;
	}
	
	public int getMonthsInPeriod() {
		return monthsInPeriod;
	}
}
