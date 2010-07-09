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


/**
 * Interface to the bean handling the DN filter logic.
 * 
 * @author Jan Van den Bergh
 */
public interface DistinguishedNameManager {

	static final String NAME = "be.fedict.eid.pkira.dn.dnManager";

	/**
	 * Parses a DN and converts it to a DistinguishedName.
	 * 
	 * @param expression
	 *            expression to parse.
	 * @return the distinguished name.
	 * @throws InvalidDistinguishedNameException
	 *             if the expression in invalid.
	 */
	DistinguishedName createDistinguishedName(String dn) throws InvalidDistinguishedNameException;

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
	String normalizeDistinguishedNameExpression(String dnExpression) throws InvalidDistinguishedNameException;

	/**
	 * Parses a DN expression and converts it to a DistinguishedName.
	 * 
	 * @param expression
	 *            expression to parse.
	 * @return the distinguished name.
	 * @throws InvalidDistinguishedNameException
	 *             if the expression in invalid.
	 */
	DistinguishedNameExpression createDistinguishedNameExpression(String expression) throws InvalidDistinguishedNameException;
}
