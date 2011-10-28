package be.fedict.eid.pkira.common.util;

import java.util.Date;

public interface FilterHelper {

	public static final String NAME = "be.fedict.eid.pkira.common.filterHelper";

	boolean matchWildcardExpression(String expression, String value);

	boolean matchRegularExpression(String expression, String value);

	boolean filterByDate(Date actualDate, Date dateFrom, Date dateTo);

}
