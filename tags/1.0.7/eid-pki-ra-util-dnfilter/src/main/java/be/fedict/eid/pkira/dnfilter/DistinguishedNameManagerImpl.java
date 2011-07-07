package be.fedict.eid.pkira.dnfilter;

import java.io.StringReader;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * Implementation of the DN Filter Manager.
 * 
 * @author Jan Van den Bergh
 */
@Name(DistinguishedNameManager.NAME)
@Scope(ScopeType.APPLICATION)
public class DistinguishedNameManagerImpl implements DistinguishedNameManager {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DistinguishedName createDistinguishedName(String dn) throws InvalidDistinguishedNameException {
		// Check for null
		if (dn == null) {
			throw new InvalidDistinguishedNameException("Expression is null.");
		}

		// Parse the filter
		DistinguishedNameParserState data;
		try {
			// Parse the data
			DistinguishedNameParser distinguishedNameParser = new DistinguishedNameParser(new StringReader(dn));
			data = distinguishedNameParser.distinguishedName();
		} catch (ParseException e) {
			throw new InvalidDistinguishedNameException("Invalid filter expression: " + dn, e);
		} catch (TokenMgrError e) {
			throw new InvalidDistinguishedNameException("Invalid filter expression: " + dn, e);
		}

		// Create the DistinguishedName
		return new DistinguishedName(data);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DistinguishedNameExpression createDistinguishedNameExpression(String expression)
			throws InvalidDistinguishedNameException {
		// Check for null
		if (expression == null) {
			throw new InvalidDistinguishedNameException("Expression is null.");
		}

		// Parse the filter
		DistinguishedNameParserState data;
		try {
			// Parse the data
			DistinguishedNameParser distinguishedNameParser = new DistinguishedNameParser(new StringReader(expression));
			data = distinguishedNameParser.distinguishedNameExpression();
		} catch (ParseException e) {
			throw new InvalidDistinguishedNameException("Invalid filter expression: " + expression, e);
		} catch (TokenMgrError e) {
			throw new InvalidDistinguishedNameException("Invalid filter expression: " + expression, e);
		}

		// Create the DistinguishedName
		return new DistinguishedNameExpression(data);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String normalizeDistinguishedNameExpression(String dnExpression) throws InvalidDistinguishedNameException {
		return createDistinguishedNameExpression(dnExpression).toString();
	}

}
