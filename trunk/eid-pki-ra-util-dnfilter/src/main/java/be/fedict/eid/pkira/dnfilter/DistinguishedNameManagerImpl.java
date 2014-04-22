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

package be.fedict.eid.pkira.dnfilter;

import java.io.StringReader;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * Implementation of the DN Filter Manager.
 * 
 * @author Jan Van den Bergh
 */
@Name(DistinguishedNameManager.NAME)
@Scope(ScopeType.APPLICATION)
public class DistinguishedNameManagerImpl implements DistinguishedNameManager {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DistinguishedName createDistinguishedName(String dn) throws InvalidDistinguishedNameException {
		// Check for null
		if (dn == null) {
			throw new InvalidDistinguishedNameException("Expression is null.");
		}

		// Parse the filter
		try {
			// Parse the data
			DistinguishedNameParser distinguishedNameParser = new DistinguishedNameParser(new StringReader(dn));
			return distinguishedNameParser.distinguishedName();
		} catch (ParseException e) {
			throw new InvalidDistinguishedNameException("Invalid dn: " + dn, e);
		} catch (TokenMgrError e) {
			throw new InvalidDistinguishedNameException("Invalid dn: " + dn, e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DistinguishedNameExpression createDistinguishedNameExpression(String expression)
			throws InvalidDistinguishedNameException {
		// Check for null
		if (expression == null) {
			throw new InvalidDistinguishedNameException("Expression is null.");
		}

		// Parse the filter
		try {
			// Parse the data
			DistinguishedNameParser distinguishedNameParser = new DistinguishedNameParser(new StringReader(expression));
			return distinguishedNameParser.distinguishedNameExpression();
		} catch (ParseException e) {
			throw new InvalidDistinguishedNameException("Invalid filter expression: " + expression, e);
		} catch (TokenMgrError e) {
			throw new InvalidDistinguishedNameException("Invalid filter expression: " + expression, e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String normalizeDistinguishedNameExpression(String dnExpression) throws InvalidDistinguishedNameException {
		return createDistinguishedNameExpression(dnExpression).toString();
	}

}
