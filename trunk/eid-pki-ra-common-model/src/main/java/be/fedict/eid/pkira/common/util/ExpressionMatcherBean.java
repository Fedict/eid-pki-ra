package be.fedict.eid.pkira.common.util;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name(ExpressionMatcherBean.NAME)
@Scope(ScopeType.STATELESS)
public class ExpressionMatcherBean implements ExpressionMatcher {

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
