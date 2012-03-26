package be.fedict.eid.pkira.common.util;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Date;

import org.testng.annotations.Test;

public class FilterHelperBeanTest {

	private final FilterHelper filterHelper = new FilterHelperBean();
	
	@Test
	public void matchRegularExpression() {
		assertTrue(filterHelper.matchRegularExpression("a+", "aaa"));
		assertFalse(filterHelper.matchRegularExpression("aa+", "a"));
	}

	@Test
	public void matchWildcardExpression() {
		assertTrue(filterHelper.matchWildcardExpression("[a]*", "[a]ba"));
		assertFalse(filterHelper.matchWildcardExpression("a*?", "a"));
	}
	
	public void filterByDate() {
		Date date = new Date();
		assertTrue(filterHelper.filterByDate(date, date, date));
	}
}
