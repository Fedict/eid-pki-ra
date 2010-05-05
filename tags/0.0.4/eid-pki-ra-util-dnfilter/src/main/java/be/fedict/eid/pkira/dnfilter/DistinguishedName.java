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

import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 * A filter used for filtering distinguished names.
 * 
 * @author Jan Van den Bergh
 */
public class DistinguishedName {

	public static final String WILDCARD = "*";

	/**
	 * State of the DistinguishedName is stored as a map of lists. The map
	 * should be sorted, so a SortedMap
	 */
	private final SortedMap<String, List<String>> data;

	/**
	 * Package protected constructor (called from DNFilterManagerImpl).
	 */
	DistinguishedName(SortedMap<String, List<String>> data) {
		this.data = data;
	}

	/**
	 * Returns the number of items in the filter.
	 */
	public int getSize() {
		int size=0;
		for (List<String> values : data.values()) {
			size+=values.size();
		}
		return size;
	}

	/**
	 * Checks if this DistinguishedName has wildcards.
	 */
	public boolean hasWildcard() {
		for (List<String> values : data.values()) {
			for (String value : values) {
				if (WILDCARD.equals(value)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Checks if this DN matches another one.
	 */
	public boolean matches(DistinguishedName otherDN) {
		if (otherDN==null || otherDN.data.size()!=data.size()) {
			return false;
		}
		
		for(Map.Entry<String,List<String>> entry: data.entrySet()) {
			List<String> values = entry.getValue();
			List<String> otherValues = otherDN.data.get(entry.getKey());
			if (otherValues==null || !matches(values, otherValues)) {
				return false;
			}
		}
		
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		for (Map.Entry<String, List<String>> entry : data.entrySet()) {
			for (String value : entry.getValue()) {
				if (builder.length() != 0) {
					builder.append(',');
				}
				builder.append(entry.getKey());
				builder.append('=');
				builder.append(value);
			}
		}
		
		return builder.toString();
	}

	private boolean matches(List<String> values, List<String> otherValues) {
		if (values.size()!=otherValues.size()) {
			return false;
		}
		
		for(int i=0; i<values.size(); i++) {
			String value = values.get(i);
			String otherValue = otherValues.get(i);
			if (!WILDCARD.equals(value) && !WILDCARD.equals(otherValue) && !otherValue.equals(value)) {
				return false;
			}
		}

		return true;
	}

}
