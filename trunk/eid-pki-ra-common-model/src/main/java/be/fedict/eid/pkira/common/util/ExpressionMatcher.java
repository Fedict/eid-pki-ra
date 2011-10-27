package be.fedict.eid.pkira.common.util;

public interface ExpressionMatcher {

	public static final String NAME = "be.fedict.eid.pkira.common.expressionMatcher";

	boolean matchWildcardExpression(String expression, String value);

	boolean matchRegularExpression(String expression, String value);

}
