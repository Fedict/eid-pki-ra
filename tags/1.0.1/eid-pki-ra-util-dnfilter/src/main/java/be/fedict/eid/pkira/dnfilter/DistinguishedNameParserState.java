package be.fedict.eid.pkira.dnfilter;

import java.util.Set;
import java.util.TreeSet;

public class DistinguishedNameParserState {

	public class StateElement implements Comparable<StateElement> {

		private final String name;
		private final String value;
		private final boolean nameWildcard;
		private final boolean valueWildcard;

		public StateElement(String name, String value) {
			if (name.startsWith("*") && isExpression()) {
				this.nameWildcard = true;
				this.name = name.substring(1);
			} else {
				this.name = name;
				this.nameWildcard = false;
			}
			if (value.equals("*") && isExpression()) {
				this.valueWildcard = true;
				this.value = null;
			} else {
				this.valueWildcard = false;
				
				StringBuilder valueBuilder = new StringBuilder();
				for(int i=0; i<value.length(); i++) {
					char ch = value.charAt(i);
					if (ch!='\\') {
						valueBuilder.append(ch);
					} else {
						char ch1 = value.charAt(i+1);
						if (ch1=='\\' || ch1==',') {
							valueBuilder.append('\\');
						}
						valueBuilder.append(ch1);
						i++;
					}
					
				}
				this.value = valueBuilder.toString();
			}
		}

		public String getName() {
			return name;
		}

		public String getValue() {
			return value;
		}

		public boolean isNameWildcard() {
			return nameWildcard;
		}

		public boolean isValueWildcard() {
			return valueWildcard;
		}

		@Override
		public int compareTo(StateElement other) {
			if (other == null) {
				return 1;
			}
			if (other == this) {
				return 0;
			}

			// name wildcards at the end
			if (nameWildcard && !other.nameWildcard) {
				return 1;
			}
			if (!nameWildcard && other.nameWildcard) {
				return -1;
			}

			// value wildcards in front of them
			if (isValueWildcard() && !other.isValueWildcard()) {
				return 1;
			}
			if (!isValueWildcard() && other.isValueWildcard()) {
				return -1;
			}

			// same names together
			int x = name.compareTo(other.name);
			if (x != 0) {
				return x;
			}

			// compare values
			if (!isValueWildcard()) {
				x = value.compareTo(other.value);
			}
			return x != 0 ? x : -1;
		}

		@Override
		public String toString() {
			String theValue;
			if (isValueWildcard()) {
				theValue = "*";
			} else if (isExpression()) {
				theValue = value.replaceAll("\\*", "\\\\*");
			} else {
				theValue = value;
			}
			return (isNameWildcard() ? "*" : "") + getName() + "=" + theValue;
		}

		public boolean matches(StateElement otherElement) {
			return otherElement.getName().equals(this.getName()) && (
					otherElement.isValueWildcard() ||
					isValueWildcard() ||
					otherElement.getValue().equals(getValue()));
		}
	}

	private final Set<StateElement> elements = new TreeSet<StateElement>();
	private final boolean expression;

	public DistinguishedNameParserState(boolean expression) {
		this.expression = expression;
	}

	public Set<StateElement> getElements() {
		return elements;
	}

	public boolean isExpression() {
		return expression;
	}

	public void addStateElement(String name, String value) {
		elements.add(new StateElement(name, value));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		for (StateElement element : elements) {
			if (builder.length() != 0) {
				builder.append(',');
			}
			builder.append(element.toString());
		}

		return builder.toString();
	}

}
