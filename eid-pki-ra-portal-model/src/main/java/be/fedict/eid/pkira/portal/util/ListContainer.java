/**
 * eID PKI RA Project. 
 * Copyright (C) 2010 FedICT. 
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

package be.fedict.eid.pkira.portal.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * @author Bram Baeyens
 *
 */
@Name(ListContainer.NAME)
@Scope(ScopeType.APPLICATION)
public class ListContainer implements Serializable {
	
	private static final long serialVersionUID = 2931481314588085666L;
	
	public static final String NAME = "be.fedict.eid.pkira.portal.listContainer";

	// TODO (03032010): get these from configuration table
	public List<SelectItem> getValidityPeriods() {
		List<SelectItem> validityPeriods = new ArrayList<SelectItem>();
		validityPeriods.add(new SelectItem(15, "15"));
		return validityPeriods;
	}
}
