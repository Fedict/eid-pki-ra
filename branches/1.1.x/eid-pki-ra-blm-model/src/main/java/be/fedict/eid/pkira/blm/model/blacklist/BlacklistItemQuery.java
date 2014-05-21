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

package be.fedict.eid.pkira.blm.model.blacklist;

import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.blm.model.framework.DataTableEntityQuery;

@Name(value= BlacklistItemQuery.NAME)
public class BlacklistItemQuery extends DataTableEntityQuery<BlacklistItem> {

	private static final long serialVersionUID = 2047569824505992173L;
	
	public static final String NAME = "be.fedict.eid.pkira.blm.blacklistItemQuery";

    @In(value = BlacklistItemHome.NAME, create = true)
    private BlacklistItemHome blacklistItemHome;
	
	@Override
	public String getEjbql() {
		return "select blacklistItem from BlacklistItem blacklistItem";
	}
	
	@Override
	public List<BlacklistItem> getResultList() {
		setOrderColumn("blacklistItem.blockedCN");
		return super.getResultList();
	}
}
