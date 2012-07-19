/*
 * eID PKI RA Project.
 * Copyright (C) 2010-2012 FedICT.
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
package be.fedict.eid.pkira.reports;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import be.fedict.eid.pkira.generated.reports.ContractType;
import be.fedict.eid.pkira.generated.reports.DetailType;
import be.fedict.eid.pkira.generated.reports.ObjectFactory;

/**
 * @author Jan Van den Bergh
 */
public class DetailBuilder implements Builder<DetailType> {

	private String requester;
	private String subject;
	private GregorianCalendar time;
	private ContractType type;
	private boolean success;

	public DetailBuilder withRequester(String requester) {
		this.requester = requester;
		return this;
	}

	public DetailBuilder withSubject(String subject) {
		this.subject = subject;
		return this;
	}

	public DetailBuilder withTime(Date time) {
		this.time = new GregorianCalendar();
		this.time.setTime(time);
		return this;
	}
	
	public DetailBuilder withContractType(ContractType contractType) {
		this.type = contractType;
		return this;
	}
	
	public DetailBuilder withSuccess(boolean success) {
		this.success = success;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DetailType build() {
		DetailType result = new ObjectFactory().createDetailType();
		result.setRequester(requester);
		result.setSubject(subject);
		result.setType(type);
		result.setSuccess(success);
		
		if (time != null) {
			try {
				result.setTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(time));
			} catch (DatatypeConfigurationException e) {
				throw new RuntimeException(e);
			}
		}

		return result;
	}

}
