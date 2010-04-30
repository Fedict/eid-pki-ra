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

package be.fedict.eid.pkira.portal.ra;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.log.Log;

import be.fedict.eid.pkira.common.security.EIdUserCredentials;
import be.fedict.eid.pkira.contracts.EIDPKIRAContractsClient;
import be.fedict.eid.pkira.contracts.EntityBuilder;
import be.fedict.eid.pkira.contracts.XmlMarshallingException;
import be.fedict.eid.pkira.privatews.EIDPKIRAPrivateServiceClient;

/**
 * @author Bram Baeyens
 *
 */
public abstract class AbstractPreSigningHandler<T extends AbstractSigningWrapper<? extends AbstractContract>> implements DSSPreSigningHandler {

	@Logger
	private Log log;	

	@In(create=true, value=EIDPKIRAContractsClient.NAME)
	private EIDPKIRAContractsClient contractsClientPortal;
	
	@In(create=true, value=EIDPKIRAPrivateServiceClient.NAME)
	protected EIDPKIRAPrivateServiceClient privateServiceClient;
	
	@In
	protected EIdUserCredentials credentials;
	
	protected abstract T getSigningWrapper();
	
	protected abstract void setSigningWrapper(T signingWrapper);
	
	@Override
	public String prepareSignment() {
		log.info(">>> prepareSignment(signingWrapper[{0}])" + getSigningWrapper());
		try {
			T signingWrapper = getSigningWrapper();
			String base64CsrXml = marshalBase64CsrXml(signingWrapper.getContract());
			signingWrapper.setBase64CsrXml(base64CsrXml);
		} catch (XmlMarshallingException e) {
			log.info("<<< prepareSignment: marshalling failed", e);
			throw new RuntimeException(e);
		}
		log.info("<<< prepareSignment");
		return "success";
	}
	
	protected abstract String marshalBase64CsrXml(AbstractContract contract) throws XmlMarshallingException;

	protected EIDPKIRAContractsClient getContractsClientPortal() {
		return contractsClientPortal;
	}
	
	protected EntityBuilder initBuilder(Operator operator) {
		return new EntityBuilder()
				.setName(operator.getName())
				.setFunction(operator.getFunction())
				.setPhone(operator.getPhone());
	}
}
