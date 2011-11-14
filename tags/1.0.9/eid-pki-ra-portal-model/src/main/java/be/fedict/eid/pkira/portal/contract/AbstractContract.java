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

package be.fedict.eid.pkira.portal.contract;

import java.io.Serializable;

import org.hibernate.validator.Length;
import org.jboss.seam.annotations.In;

import be.fedict.eid.pkira.portal.framework.Operator;


/**
 * @author Bram Baeyens
 *
 */
public abstract class AbstractContract implements Serializable {
	
	private static final long serialVersionUID = -4191567270718469935L;
	
	@In(value=Operator.NAME)
	private Operator operator;
	
	private String description;
	private String legalNotice = "testLegalNotice";

	@Length(max=512, message="{validator.length}")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getLegalNotice() {
		return legalNotice;
	}

	public void setLegalNotice(String legalNotice) {
		this.legalNotice = legalNotice;
	}
	
	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	@Override
	public String toString() {
		return new StringBuilder()
			.append("operator=").append(operator)
			.append(", legalNotice=").append(legalNotice)		
			.toString();
	}
}
