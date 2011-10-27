package be.fedict.eid.pkira.common.util;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

public class ExpressionMatcherBeanTest {

	private final ExpressionMatcherBean expressionMatcher = new ExpressionMatcherBean();
	
	@Test
	public void matchRegularExpression() {
		assertTrue(expressionMatcher.matchRegularExpression("a+", "aaa"));
		assertFalse(expressionMatcher.matchRegularExpression("aa+", "a"));
	}

	@Test
	public void matchWildcardExpression() {
		assertTrue(expressionMatcher.matchWildcardExpression("[a]*", "[a]ba"));
		assertFalse(expressionMatcher.matchWildcardExpression("a*?", "a"));
	}
}
