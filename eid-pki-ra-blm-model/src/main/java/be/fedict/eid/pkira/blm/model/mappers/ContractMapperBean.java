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

package be.fedict.eid.pkira.blm.model.mappers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.pkira.blm.model.contracts.AbstractContract;
import be.fedict.eid.pkira.generated.privatews.ContractWS;

/**
 * @author Bram Baeyens
 *
 */
@Name(ContractMapper.NAME)
@Scope(ScopeType.STATELESS)
public class ContractMapperBean implements Serializable, ContractMapper {

	private static final long serialVersionUID = 518966394376372668L;

	public Collection<ContractWS> map(List<AbstractContract> contracts) {
		List<ContractWS> contractWSList = new ArrayList<ContractWS>();
		for (AbstractContract contract : contracts) {
			
		}
		return contractWSList;
	}

}
