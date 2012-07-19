/**
 * eID PKI RA Project. 
 * Copyright (C) 2010-2012 FedICT. 
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

package be.fedict.eid.pkira.portal.framework;

import java.io.Serializable;

import org.hibernate.validator.Length;
import org.hibernate.validator.Pattern;
import org.jboss.seam.annotations.Name;

/**
 * @author Bram Baeyens
 *
 */
@Name(Operator.NAME)
public class Operator implements Serializable {

	private static final long serialVersionUID = -9069139272251459619L;

	public static final String NAME = "be.fedict.eid.pkira.portal.operator";

	private String name;
	private String function;
	private String phone;
	
	@Length(max=64, message="#{messages['validator.length']}")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Length(max=64, message="#{messages['validator.length']}")
	public String getFunction() {
		return function;
	}
	
	public void setFunction(String function) {
		this.function = function;
	}

	@Pattern(regex="(\\+|0)[-0-9 \\./]+", message="#{messages['validator.phone.invalid']}")
	@Length(max=32, message="#{messages['validator.length']}")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Override
	public String toString() {
		return new StringBuilder("Operator[")
				.append("name=").append(name)
				.append(", function=").append(function)
				.append(", phone=").append(phone)
				.append(']').toString();
	}
}
