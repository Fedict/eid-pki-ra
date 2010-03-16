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

package be.fedict.eid.pkira.portal.handler;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.pkira.contracts.CertificateRevocationRequestBuilder;
import be.fedict.eid.pkira.contracts.XmlMarshallingException;
import be.fedict.eid.pkira.generated.contracts.CertificateRevocationRequestType;
import be.fedict.eid.pkira.portal.domain.AbstractContract;
import be.fedict.eid.pkira.portal.domain.RevocationContract;
import be.fedict.eid.pkira.portal.domain.RevocationContractSigningWrapper;

/**
 * @author Bram Baeyens
 *
 */
@Name("revocationContractPreSigningHandler")
@Scope(ScopeType.EVENT)
public class RevocationContractPreSigningHandlerBean extends AbstractDSSPreSigningHandler<RevocationContractSigningWrapper> {

	private RevocationContractSigningWrapper signingWrapper;
	
	@Override
	@In(create=true, value="revocationContractSigningWrapper")
	protected void setSigningWrapper(RevocationContractSigningWrapper signingWrapper) {
		this.signingWrapper = signingWrapper;		
	}

	@Override
	@Out
	protected RevocationContractSigningWrapper getSigningWrapper() {
		return signingWrapper;
	}

	@Override
	protected String marshalBase64CsrXml(AbstractContract contract) throws XmlMarshallingException {
		return getContractsClientPortal().marshalToBase64(initBuilder((RevocationContract) contract).toRequestType(), CertificateRevocationRequestType.class);		
	}

	private CertificateRevocationRequestBuilder initBuilder(RevocationContract contract) {
		return new CertificateRevocationRequestBuilder()
				.setOperator(initBuilder(contract.getOperator()).toEntityType())
				.setDistinguishedName(contract.getCertificate().getDistinguishedName().getSubject())
				.setValidityStart(contract.getCertificate().getValidityStart())
				.setValidityEnd(contract.getCertificate().getValidityEnd())
				.setCertificate(contract.getCertificate().getX509())
				.setLegalNotice(contract.getLegalNotice())
				.setDescription(contract.getDescription());
	}	
}
