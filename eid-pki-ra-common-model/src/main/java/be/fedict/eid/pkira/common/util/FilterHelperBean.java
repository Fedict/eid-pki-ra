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

package be.fedict.eid.pkira.common.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name(FilterHelperBean.NAME)
@Scope(ScopeType.STATELESS)
public class FilterHelperBean implements FilterHelper {

	@Override
	public boolean matchWildcardExpression(String expression, String value) {
		return matchRegularExpression(wildcardToRegexp(expression), value);
	}

	@Override
	public boolean matchRegularExpression(String expression, String value) {
		if (StringUtils.isEmpty(value)) {
			return StringUtils.isEmpty(expression);
		}
		return value.matches(expression);
	}
	
	@Override
	public boolean filterByDate(Date actualDate, Date dateFrom, Date dateTo) {
		if (dateFrom!=null) {
			dateFrom = changeTime(dateFrom, 0, 0, 0); 
			if (dateFrom.after(actualDate)) {
				return false;
			}
		}
		if (dateTo!=null) {
			dateTo = changeTime(dateTo, 23, 59, 59); 
			if (dateTo.before(actualDate)) {
				return false;
			}
		}
		
		return true;
	}

	private Date changeTime(Date date, int hours, int minutes, int seconds) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR, hours);
		calendar.set(Calendar.MINUTE, minutes);
		calendar.set(Calendar.SECOND, seconds);

		return calendar.getTime();
	}

	private String wildcardToRegexp(String wildcard) {
		StringBuffer s = new StringBuffer(wildcard.length());
		s.append('^');
		for (int i = 0, is = wildcard.length(); i < is; i++) {
			char c = wildcard.charAt(i);
			switch (c) {
			case '*':
				s.append('.');
				s.append('*');
				break;
			case '?':
				s.append('.');
				break;
			// escape special regexp-characters
			case '(':
			case ')':
			case '[':
			case ']':
			case '$':
			case '^':
			case '.':
			case '{':
			case '}':
			case '|':
			case '\\':
			case '+':
				s.append('\\');
				s.append(c);
				break;
			default:
				s.append(c);
				break;
			}
		}
		s.append('$');
		return (s.toString());
	}
}
