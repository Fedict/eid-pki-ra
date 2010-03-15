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
import java.util.Set;

/**
 * Interface to the bean handling the DN filter logic.
 * 
 * @author Jan Van den Bergh
 */
public interface DistinguishedNameManager {

	static final String NAME = "dnFilterManager";

	/**
	 * Parses an expression and converts it to a DistinguishedName.
	 * 
	 * @param expression
	 *            expression to parse.
	 * @return the distinguished name.
	 * @throws InvalidDistinguishedNameException
	 *             if the expression in invalid.
	 */
	DistinguishedName createDistinguishedName(String expression) throws InvalidDistinguishedNameException;

	/**
	 * Checks if the given distinguished name overlaps with any of the other
	 * distinguished names.
	 * 
	 * @param newDN
	 *            the filter to check.
	 * @param otherDNs
	 *            filters to check with.
	 * @return the distinguished names this name overlaps with.
	 */
	Set<DistinguishedName> overlapsWith(DistinguishedName newDN, Collection<DistinguishedName> otherDNs);

	/**
	 * Normalizes a distinguished name expression. This puts all its parts in
	 * the correct order.
	 * 
	 * @param dnExpression
	 *            expression to normalize.
	 * @return normalized expression.
	 * @throws InvalidDistinguishedNameException
	 *             if the expression could not be parsed.
	 */
	String normalize(String dnExpression) throws InvalidDistinguishedNameException;
}
