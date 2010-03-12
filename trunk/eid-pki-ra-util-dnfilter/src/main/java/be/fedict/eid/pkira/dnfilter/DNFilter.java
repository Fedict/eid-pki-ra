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

/**
 * A filter used for filtering distinguished names.
 * 
 * @author Jan Van den Bergh
 */
public class DNFilter {

	/**
	 * Part of the filter
	 */
	public static class DNFilterPart implements Comparable<DNFilterPart> {

		final String key, value;

		public DNFilterPart(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}

		public boolean isWildcard() {
			return "*".equals(value);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compareTo(DNFilterPart o) {
			if (isWildcard() && !o.isWildcard()) {
				return 1;
			}
			if (!isWildcard() && o.isWildcard()) {
				return -1;
			}
			int result = key.compareToIgnoreCase(o.key);
			if (result!=0) {
				return result;
			}
			return value.compareTo(o.value);
		}
	}

	private final List<DNFilterPart> parts;

	/**
	 * Package protected constructor (called from DNFilterManager).
	 * 
	 * @param filterParts
	 *            parts the filter consists of.
	 */
	public DNFilter(List<DNFilterPart> parts) {
		this.parts = parts;
	}

	/**
	 * Returns the number of items in the filter.
	 */
	public int getSize() {
		return parts.size();
	}

	/**
	 * Retrieves a part from the filter.
	 */
	public DNFilterPart getPart(int index) {
		return parts.get(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for(DNFilterPart part: parts) {
			if (builder.length() != 0) {
				builder.append(',');
			}
			builder.append(part.getKey());
			builder.append('=');
			builder.append(part.getValue());
		}
		return builder.toString();
	}

}
