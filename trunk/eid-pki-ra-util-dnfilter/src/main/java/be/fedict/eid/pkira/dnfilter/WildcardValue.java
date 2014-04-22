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

/**
 * Value that can contain a wildcard at the start.
 */
class WildcardValue implements Comparable<WildcardValue> {

	private final String value;
	private final boolean wildcard;

	public WildcardValue(String value) {
		if (value.startsWith("*")) {
			this.wildcard = true;
			this.value = Util.unescape(value.substring(1));
		} else {
			this.value = Util.unescape(value);
			this.wildcard = false;
		}
	}

	@Override
	public int compareTo(WildcardValue other) {
		if (other == null) {
			return 1;
		}
		if (other == this) {
			return 0;
		}

		// wildcards at the end
		if (wildcard && !other.wildcard) {
			return 1;
		}
		if (!wildcard && other.wildcard) {
			return -1;
		}

		// otherwise sort names
		return value.compareTo(other.value);
	}

	public String getValue() {
		return value;
	}

	public boolean isWildcard() {
		return wildcard;
	}

	@Override
	public String toString() {
		return (wildcard ? "*" : "") + Util.escape(value, "()|*,\\=");
	}

	public boolean matches(String value) {
		if (value == null) {
			return false;
		}
		if (value.equals(this.value)) {
			return true;
		}
		if (this.wildcard && value.endsWith(this.value)) {
			return true;
		}
		return false;
	}

	public boolean overlaps(WildcardValue other) {
		if (value.equals(other.value)) {
			return true;
		}
		if (wildcard && other.value.endsWith(value)) {
			return true;
		}
		if (other.wildcard && value.endsWith(other.value)) {
			return true;
		}
		return false;
	}

}
