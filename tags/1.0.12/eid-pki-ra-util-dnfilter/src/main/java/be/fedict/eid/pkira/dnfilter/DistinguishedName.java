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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A distinguished names. It doesn't contain any wildcards.
 * 
 * @author Jan Van den Bergh
 */
public class DistinguishedName {

	static class DistinguishedNameElement implements Comparable<DistinguishedNameElement> {

		private final String name;
		private final String value;

		public DistinguishedNameElement(String name, String value) {
			this.name = name;
			this.value = Util.unescape(value);
		}

		@Override
		public int compareTo(DistinguishedNameElement other) {
			if (other == null) {
				return 1;
			}
			if (other == this) {
				return 0;
			}

			// compare names
			int x = name.compareTo(other.name);
			if (x != 0) {
				return x;
			}

			x = value.compareTo(other.value);
			return x!=0 ? x : 0; // never equal
		}

		public String getName() {
			return name;
		}

		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return name + "=" + Util.escape(value, "\\,=");
		}
	}

	private final SortedSet<DistinguishedNameElement> elements = new TreeSet<DistinguishedNameElement>();

	/**
	 * Package protected constructor (called from DNFilterManagerImpl).
	 */
	DistinguishedName() {
	}
	
	DistinguishedName(DistinguishedName other) {
		elements.addAll(other.elements);
	}

	Set<DistinguishedNameElement> getElements() {
		return elements;
	}

	int getSize() {
		return elements.size();
	}
	
	/**
	 * Returns a list of all the parts with this name.
	 */
	public Set<String> getPart(String name) {
		if (name==null) {
			return Collections.emptySet();
		}
		
		Set<String> result = new HashSet<String>();
		name = name.toLowerCase();
		for(DistinguishedNameElement element: elements) {
			if (element.getName().equals(name)) {
				result.add(element.getValue());
			}
		}
		
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		for (DistinguishedNameElement element : elements) {
			if (builder.length() != 0) {
				builder.append(',');
			}
			builder.append(element);
		}

		return builder.toString();
	}

	void addElement(String name, String value) {
		elements.add(new DistinguishedNameElement(name, value));
	}

	public DistinguishedName replacePart(String name, String value) {
		DistinguishedName result = new DistinguishedName(this);
		Set<DistinguishedNameElement> elementsToRemove = new HashSet<DistinguishedName.DistinguishedNameElement>();
		for(DistinguishedNameElement element: result.elements) {
			if (element.name.equals(name)) { elementsToRemove.add(element); }
		}
		result.elements.removeAll(elementsToRemove);
		
		result.elements.add(new DistinguishedNameElement(name, value));
		
		return result;
	}

}
