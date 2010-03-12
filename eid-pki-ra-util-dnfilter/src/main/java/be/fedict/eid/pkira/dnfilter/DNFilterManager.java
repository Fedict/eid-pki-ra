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
package be.fedict.eid.pkira.dnfilter;

import java.util.Collection;

/**
 * Interface to the bean handling the DN filter logic.
 * 
 * @author Jan Van den Bergh
 */
public interface DNFilterManager {

	static final String NAME = "dnFilterManager";
	
	/**
	 * Parses a filter expression and converts it to a DNFilter.
	 * 
	 * @param filterExpression
	 *            expression to parse.
	 * @return the filter.
	 * @throws InvalidDNFilterException
	 *             if the filter string in invalid.
	 */
	DNFilter createDNFilter(String filterExpression) throws InvalidDNFilterException;

	/**
	 * Checks if the given filter overlaps with any of the other filters.
	 * 
	 * @param newFilter
	 *            the filter to check.
	 * @param otherFilters
	 *            filters to check with.
	 * @return if there is an overlap.
	 */
	boolean overlaps(DNFilter newFilter, Collection<DNFilter> otherFilters);

	/**
	 * Checks if a DN matches a filter.
	 * 
	 * @param filter
	 *            filter to check matches with.
	 * @param dnExpression
	 *            the expression to match.
	 * @return if there is a match.
	 */
	boolean matches(DNFilter filter, String dnExpression);

	/**
	 * Normalizes a filter expressions. This puts all the expressions in the correct order.
	 * @param filterExpression filter expression to normalize.
	 * @return normalized expression.
	 * @throws InvalidDNFilterException if the filter expression could not be parsed.
	 */
	String normalize(String filterExpression) throws InvalidDNFilterException;
}
