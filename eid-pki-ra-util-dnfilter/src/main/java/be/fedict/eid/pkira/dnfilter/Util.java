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

class Util {

	public static String escape(String value, String specialChars) {
		StringBuilder valueBuilder = new StringBuilder();
		for (int i = 0; i < value.length(); i++) {
			char ch = value.charAt(i);
			if (specialChars.indexOf(ch) != -1) {
				valueBuilder.append('\\');
			}
			valueBuilder.append(ch);
		}

		return valueBuilder.toString();
	}
	
	public static String unescape(String value) {
		StringBuilder valueBuilder = new StringBuilder();
		for (int i = 0; i < value.length(); i++) {
			char ch = value.charAt(i);
			if (ch != '\\') {
				valueBuilder.append(ch);
			} else {
				ch = value.charAt(i + 1);
				valueBuilder.append(ch);
				i++;
			}
		}

		return valueBuilder.toString();
	}
}
