package be.fedict.eid.pkira.dnfilter;

import java.io.StringReader;
import java.util.Collection;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * Implementation of the DN Filter Manager.
 * 
 * @author Jan Van den Bergh
 */
@Name(DNFilterManager.NAME)
@Scope(ScopeType.APPLICATION)
public class DNFilterManagerBean implements DNFilterManager {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DNFilter createDNFilter(String filterExpression) throws InvalidDNFilterException {
		// Parse the filter
		if (filterExpression==null) {
			throw new InvalidDNFilterException("Filter expression is null.");
		}
		try {
			return new DNFilterParser(new StringReader(filterExpression)).filter();
		} catch (ParseException e) {
			throw new InvalidDNFilterException("Invalid filter expression: " + filterExpression, e);
		} catch (TokenMgrError e) {
			throw new InvalidDNFilterException("Invalid filter expression: " + filterExpression, e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean matches(DNFilter filter, String dnExpression) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String normalize(String filterExpression) throws InvalidDNFilterException {
		return createDNFilter(filterExpression).toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean overlaps(DNFilter newFilter, Collection<DNFilter> otherFilters) {
		// TODO Auto-generated method stub
		return false;
	}

}
