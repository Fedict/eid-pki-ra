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

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import be.fedict.eid.pkira.dnfilter.DistinguishedNameParserState.StateElement;

/**
 * A filter used for filtering distinguished names.
 * 
 * @author Jan Van den Bergh
 */
public class DistinguishedNameExpression {

	/**
	 * State of the DistinguishedName is stored as a map of lists. The map
	 * should be sorted, so a SortedMap
	 */
	private final DistinguishedNameParserState data;

	/**
	 * Package protected constructor (called from DNFilterManagerImpl).
	 */
	DistinguishedNameExpression(DistinguishedNameParserState data) {
		this.data = data;
	}

	/**
	 * Checks if this DN matches another one.
	 */
	public boolean matches(DistinguishedName otherDN) {
		// // Keep track of matched expression elements
		// Set<StateElement> otherElements = new
		// TreeSet<StateElement>(otherDN.getData().getElements());
		//
		// // Build map of matching expressions for each element of the dn
		// Map<StateElement, Set<StateElement>> matchingElements = new
		// HashMap<StateElement, Set<StateElement>>();
		// for (StateElement otherElement : otherElements) {
		// TreeSet<StateElement> theSet = new TreeSet<StateElement>();
		// matchingElements.put(otherElement, theSet);
		// for (StateElement element : data.getElements()) {
		// if (element.matches(otherElement)) {
		// theSet.add(element);
		// }
		// }
		// }
		//
		// // Clean up the map
		// while (!matchingElements.isEmpty()) {
		// // Look for elements with 1 or 0 matching expressions and handle
		// // them
		// StateElement key = null;
		// StateElement value = null;
		// for (Map.Entry<StateElement, Set<StateElement>> entry :
		// matchingElements.entrySet()) {
		// if (entry.getValue().size() == 0) {
		// return false;
		// }
		// if (entry.getValue().size() == 1) {
		// key = entry.getKey();
		// value = entry.getValue().iterator().next();
		// break;
		// }
		// }
		//
		// // Remove the element
		// if (key != null) {
		// if (!value.isNameWildcard()) {
		// for (Set<StateElement> set : matchingElements.values()) {
		// set.remove(value);
		// }
		// }
		// matchingElements.remove(key);
		// } else {
		// throw new RuntimeException("Inconclusive");
		// }
		// }
		//
		// return true;

		Set<StateElement> elementsToMatch = new TreeSet<StateElement>(data.getElements());
		// For each element in the other DN...
		otherElement: for (StateElement otherElement : otherDN.getData().getElements()) {
			// ...look for a matching element here...
			for (StateElement element : elementsToMatch) {
				if (element.getName().equals(otherElement.getName())
						&& (element.isValueWildcard() || element.getValue().equals(otherElement.getValue()))) {
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
		for (StateElement element : elementsToMatch) {
			if (!element.isNameWildcard()) {
				return false;
			}
		}
		return true;
	}

	public boolean overlaps(DistinguishedNameExpression other) {
		Set<StateElement> myElements = new TreeSet<StateElement>(data.getElements());
		Set<StateElement> otherElements = new TreeSet<StateElement>(other.data.getElements());

		// Remove identical non-wildcard expressions
		removeIdenticalNonWildcardExpressions(myElements, otherElements);

		// Remove non-wildcard expressions using value wildcard expressions
		removeNonWildcardWithValueWildcardExpressions(myElements, otherElements);
		removeNonWildcardWithValueWildcardExpressions(otherElements, myElements);

		// Remove value wildcard expressions against eachother
		removeIdenticalValueWildcardExpressions(myElements, otherElements);

		// Remove remaining non-wildcard expressions against name wildcard
		// expressions
		removeNotWildcardWithNameWildcardExpressions(myElements, otherElements);
		removeNotWildcardWithNameWildcardExpressions(otherElements, myElements);

		// Remove remaining value wildcard expressions against name-value
		// wildcard expressions
		removeValueWildcardWithNameValueWildcardExpressions(myElements, otherElements);
		removeValueWildcardWithNameValueWildcardExpressions(otherElements, myElements);

		// Remove remaining name wildcard expressions
		removeNameWildcardExpressions(myElements);
		removeNameWildcardExpressions(otherElements);

		return myElements.isEmpty() && otherElements.isEmpty();
	}

	private void removeNameWildcardExpressions(Set<StateElement> elements) {
		Set<StateElement> toRemove = new HashSet<StateElement>();
		for (StateElement element : elements) {
			if (element.isNameWildcard()) {
				toRemove.add(element);
			}
		}
		elements.removeAll(toRemove);
	}

	private void removeValueWildcardWithNameValueWildcardExpressions(Set<StateElement> myElements,
			Set<StateElement> otherElements) {
		Set<StateElement> toRemove = new HashSet<StateElement>();
		for (StateElement element : myElements) {
			if (!element.isNameWildcard() && element.isValueWildcard()) {
				for (StateElement otherElement : otherElements) {
					if (otherElement.isNameWildcard() && element.getName().equals(otherElement.getName())) {
						toRemove.add(element);
						break;
					}
				}
			}
		}
		myElements.removeAll(toRemove);
	}

	private void removeNotWildcardWithNameWildcardExpressions(Set<StateElement> myElements,
			Set<StateElement> otherElements) {
		Set<StateElement> toRemove = new HashSet<StateElement>();
		for (StateElement element : myElements) {
			if (!element.isNameWildcard() && !element.isValueWildcard()) {
				for (StateElement otherElement : otherElements) {
					if (otherElement.isNameWildcard()
							&& element.getName().equals(otherElement.getName())) {
						toRemove.add(element);
						break;
					}
				}
			}
		}
		myElements.removeAll(toRemove);
	}

	private void removeIdenticalValueWildcardExpressions(Set<StateElement> myElements, Set<StateElement> otherElements) {
		Set<StateElement> toRemove = new HashSet<StateElement>();
		for (StateElement element : myElements) {
			if (!element.isNameWildcard() && element.isValueWildcard()) {
				for (StateElement otherElement : otherElements) {
					if (!otherElement.isNameWildcard() && otherElement.isValueWildcard()
							&& element.getName().equals(otherElement.getName())) {
						otherElements.remove(otherElement);
						toRemove.add(element);
						break;
					}
				}
			}
		}
		myElements.removeAll(toRemove);
	}

	private void removeNonWildcardWithValueWildcardExpressions(Set<StateElement> myElements,
			Set<StateElement> otherElements) {
		Set<StateElement> toRemove = new HashSet<StateElement>();
		for (StateElement element : myElements) {
			if (!element.isNameWildcard() && !element.isValueWildcard()) {
				for (StateElement otherElement : otherElements) {
					if (!otherElement.isNameWildcard() && otherElement.isValueWildcard()
							&& element.getName().equals(otherElement.getName())) {
						otherElements.remove(otherElement);
						toRemove.add(element);
						break;
					}
				}
			}
		}
		myElements.removeAll(toRemove);
	}

	private void removeIdenticalNonWildcardExpressions(Set<StateElement> myElements, Set<StateElement> otherElements) {
		Set<StateElement> toRemove = new HashSet<StateElement>();
		for (StateElement element : myElements) {
			if (!element.isNameWildcard() && !element.isValueWildcard()) {
				for (StateElement otherElement : otherElements) {
					if (!otherElement.isNameWildcard() && !otherElement.isValueWildcard()
							&& element.getName().equals(otherElement.getName())
							&& element.getValue().equals(otherElement.getValue())) {
						otherElements.remove(otherElement);
						toRemove.add(element);
						break;
					}
				}
			}
		}
		myElements.removeAll(toRemove);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return data.toString();
	}

	public int getSize() {
		return data.getElements().size();
	}

}
