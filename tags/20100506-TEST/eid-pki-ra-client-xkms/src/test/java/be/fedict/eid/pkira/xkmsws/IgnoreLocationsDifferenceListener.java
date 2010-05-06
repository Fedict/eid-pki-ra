/*
 * eID PKI RA Project.
 * Copyright (C) 2010 FedICT.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version
 * 3.0 as published by the Free Software Foundation.
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, see
 * http://www.gnu.org/licenses/.
 */
package be.fedict.eid.pkira.xkmsws;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceListener;

/**
 * @author Jan Van den Bergh
 */
public class IgnoreLocationsDifferenceListener extends DetailedDiff implements DifferenceListener {

	private Set<String> locationsToIgnore;

	public IgnoreLocationsDifferenceListener(Diff prototype, String... locationsToIgnore) {
		super(prototype);

		this.locationsToIgnore = new HashSet<String>();
		for (String location : locationsToIgnore) {
			this.locationsToIgnore.add(location);
		}
	}

	@Override
	public int differenceFound(Difference difference) {
		// Ignore namespace prefix differences
		if ("namespace prefix".equals(difference.getDescription())) {
			return Diff.RETURN_IGNORE_DIFFERENCE_NODES_SIMILAR;
		}
		
		String testXpath = difference.getTestNodeDetail().getXpathLocation();
		String controlXpath = difference.getControlNodeDetail().getXpathLocation();
		System.err.println(">>> Difference in XML @ " + controlXpath + ": " + difference.getDescription());
		if (StringUtils.equals(testXpath, controlXpath) && locationsToIgnore.contains(controlXpath)) {
			return Diff.RETURN_IGNORE_DIFFERENCE_NODES_SIMILAR;
		}

		return super.differenceFound(difference);
	}

}
