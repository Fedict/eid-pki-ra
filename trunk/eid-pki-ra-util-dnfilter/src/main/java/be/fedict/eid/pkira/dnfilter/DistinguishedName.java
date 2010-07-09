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

import be.fedict.eid.pkira.dnfilter.DistinguishedNameParserState.StateElement;

/**
 * A distinguished names. It doesn't contain any wildcards.
 * 
 * @author Jan Van den Bergh
 */
public class DistinguishedName {

	/**
	 * State of the DistinguishedName is stored as a map of lists. The map
	 * should be sorted, so a SortedMap
	 */
	private final DistinguishedNameParserState data;

	/**
	 * Package protected constructor (called from DNFilterManagerImpl).
	 */
	DistinguishedName(DistinguishedNameParserState data) {
		this.data = data;
	}
	
	DistinguishedNameParserState getData() {
		return data;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		for (StateElement element : data.getElements()) {
			if (builder.length() != 0) {
				builder.append(',');
			}
			builder.append(element.getName());
			builder.append('=');
			builder.append(element.getValue());

		}

		return builder.toString();
	}

	public int getSize() {
		return data.getElements().size();
	}

}
