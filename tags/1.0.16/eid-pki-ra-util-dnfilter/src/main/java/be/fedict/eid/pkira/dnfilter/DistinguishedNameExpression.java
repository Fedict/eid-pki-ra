/*
 * eID PKI RA Project.
 * Copyright (C) 2010-2012 FedICT.
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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import be.fedict.eid.pkira.dnfilter.DistinguishedName.DistinguishedNameElement;

/**
 * A filter used for filtering distinguished names.
 * 
 * @author Jan Van den Bergh
 */
public class DistinguishedNameExpression {

	static class DistinguishedNameExpressionElement implements Comparable<DistinguishedNameExpressionElement> {

		private final WildcardValue name;
		private final SortedSet<WildcardValue> values;

		public DistinguishedNameExpressionElement(String name, List<String> values) {
			this.name = new WildcardValue(name);
			this.values = new TreeSet<WildcardValue>();
			for (String value : values) {
				this.values.add(new WildcardValue(value));
			}
		}

		@Override
		public int compareTo(DistinguishedNameExpressionElement other) {
			if (other == null) {
				return 1;
			}
			if (other == this) {
				return 0;
			}

			// Wildcards at end
			if (isNameWildcard() && !other.isNameWildcard()) {
				return 1;
			}
			if (!isNameWildcard() && other.isNameWildcard()) {
				return -1;
			}
			if (isValueWildcard() && !other.isValueWildcard()) {
				return 1;
			}
			if (!isValueWildcard() && other.isValueWildcard()) {
				return -1;
			}

			// compare names
			int x = name.getValue().compareTo(other.name.getValue());
			if (x != 0) {
				return x;
			}

			// compare values
			if (values.size() < other.values.size()) {
				return -1;
			}
			if (values.size() > other.values.size()) {
				return 1;
			}
			for (Iterator<WildcardValue> it1 = values.iterator(), it2 = other.values.iterator(); it1.hasNext();) {
				x = it1.next().compareTo(it2.next());
				if (x != 0) {
					return x;
				}
			}

			return -1; // never equal!
		}

		public WildcardValue getName() {
			return name;
		}

		public Set<WildcardValue> getValues() {
			return values;
		}

		public boolean isNameWildcard() {
			return name.isWildcard();
		}

		public boolean isValueWildcard() {
			for (WildcardValue value : values) {
				if (value.isWildcard()) {
					return true;
				}
			}
			return false;
		}

		public boolean matches(DistinguishedNameElement dnElement) {
			// check names
			if (!name.matches(dnElement.getName())) {
				return false;
			}

			// check values
			for (WildcardValue value : values) {
				if (value.matches(dnElement.getValue())) {
					return true;
				}
			}

			return false;
		}

		public boolean overlaps(DistinguishedNameExpressionElement otherElement) {
			// check the name
			if (!name.overlaps(otherElement.name)) {
				return false;
			}

			// check the values
			for (WildcardValue value : values) {
				for (WildcardValue otherValue : otherElement.values) {
					if (value.overlaps(otherValue)) {
						return true;
					}
				}
			}

			return false;
		}

		@Override
		public String toString() {
			StringBuilder result = new StringBuilder();
			result.append(name).append('=');

			if (values.size() != 1) {
				result.append('(');
			}

			boolean first = true;
			for (WildcardValue value : values) {
				if (!first) {
					result.append('|');
				}
				first = false;
				result.append(value);
			}

			if (values.size() != 1) {
				result.append(')');
			}

			return result.toString();
		}
	}

	private final SortedSet<DistinguishedNameExpressionElement> elements = new TreeSet<DistinguishedNameExpressionElement>();

	/**
	 * Package protected constructor (called from DNFilterManagerImpl).
	 */
	DistinguishedNameExpression() {
	}

	public void addElement(String name, List<String> values) {
		elements.add(new DistinguishedNameExpressionElement(name, values));
	}

	public void addElement(String name, String value) {
		addElement(name, Collections.singletonList(value));
	}

	public int getSize() {
		return elements.size();
	}

	/**
	 * Checks if this DN matches another one.
	 */
	public boolean matches(DistinguishedName dn) {
		Set<DistinguishedNameExpressionElement> elementsToMatch = new TreeSet<DistinguishedNameExpressionElement>(
				elements);

		// For each element in the other DN...
		otherElement: for (DistinguishedNameElement otherElement : dn.getElements()) {
			// ...look for a matching element here...
			// Note: because of the sorting, this works from more specific to
			// less specific (wildcard)
			for (DistinguishedNameExpressionElement element : elementsToMatch) {
				if (element.matches(otherElement)) {
					// ...and remove it from the elements to match if it was
					// found and is no wildcard...
					if (!element.isNameWildcard()) {
						elementsToMatch.remove(element);
					}

					// ...then continue with the next element in the DN
					continue otherElement;
				}
			}

			// if the element was not matched, indicate failure
			return false;
		}

		// If only name wildcards are left (they are not removed from the
		// collection), we are successful!
		for (DistinguishedNameExpressionElement element : elementsToMatch) {
			if (!element.isNameWildcard()) {
				return false;
			}
		}
		return true;
	}

	public boolean overlaps(DistinguishedNameExpression other) {
		// Go through our elements one by one (is from more specific to less
		// specific)
		// See if matching other element and remove it there if it is not a name
		// wildcard.
		// Check if anything remains.

		// TODO check if arguments can be switched.

		SortedSet<DistinguishedNameExpressionElement> remainingOtherElements = new TreeSet<DistinguishedNameExpressionElement>(
				other.elements);
		for (DistinguishedNameExpressionElement element : elements) {
			boolean matched = element.isNameWildcard();
			
			Set<DistinguishedNameExpressionElement> othersToRemove = new HashSet<DistinguishedNameExpression.DistinguishedNameExpressionElement>();
			for (DistinguishedNameExpressionElement otherElement : remainingOtherElements) {
				if (element.overlaps(otherElement)) {
					matched = true;
					if (!otherElement.isNameWildcard()) {
						othersToRemove.add(otherElement);
					}
					if (!element.isNameWildcard()) {
						break;
					}
				}
			}
			remainingOtherElements.removeAll(othersToRemove);

			// no match found if not name wildcard
			if (!matched) {
				return false;
			}
		}

		// If non name-wildcard elements remain, there is no overlap
		for (DistinguishedNameExpressionElement otherElement : remainingOtherElements) {
			if (!otherElement.isNameWildcard()) {
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

		for (DistinguishedNameExpressionElement element : elements) {
			if (builder.length() != 0) {
				builder.append(',');
			}
			builder.append(element.toString());
		}

		return builder.toString();
	}

	Set<DistinguishedNameExpressionElement> getElements() {
		return elements;
	}

}
