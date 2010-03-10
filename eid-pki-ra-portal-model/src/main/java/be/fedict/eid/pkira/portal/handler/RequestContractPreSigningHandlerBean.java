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

import be.fedict.eid.pkira.contracts.CertificateSigningRequestBuilder;
import be.fedict.eid.pkira.contracts.XmlMarshallingException;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningRequestType;
import be.fedict.eid.pkira.generated.contracts.CertificateTypeType;
import be.fedict.eid.pkira.portal.domain.RequestContractSigningWrapper;
import be.fedict.eid.pkira.portal.domain.AbstractContract;
import be.fedict.eid.pkira.portal.domain.RequestContract;

/**
 * @author Bram Baeyens
 *
 */
@Name("requestContractPreSigningHandler")
@Scope(ScopeType.EVENT)
public class RequestContractPreSigningHandlerBean extends AbstractDSSPreSigningHandler<RequestContractSigningWrapper> {

	private RequestContractSigningWrapper signingWrapper;

	@Override
	@Out
	protected RequestContractSigningWrapper getSigningWrapper() {
		return signingWrapper;
	}

	@Override
	@In(create=true, value="requestContractSigningWrapper")
	public void setSigningWrapper(RequestContractSigningWrapper signingWrapper) {
		this.signingWrapper = signingWrapper;		
	}

	@Override
	protected String marshalBase64CsrXml(AbstractContract contract) throws XmlMarshallingException {
		CertificateSigningRequestBuilder builder = initBuilder((RequestContract) contract);
		CertificateSigningRequestType requestType = builder.toRequestType();
		String base64Xml = getContractsClientPortal().marshalToBase64(requestType, CertificateSigningRequestType.class);
		return base64Xml;
	}
	
	private CertificateSigningRequestBuilder initBuilder(RequestContract contract) {
		return new CertificateSigningRequestBuilder()
				.setOperator(initBuilder(contract.getOperator()).toEntityType())
				.setLegalNotice(contract.getLegalNotice())
				.setDistinguishedName(contract.getDistinguishedName().getSubject())
				.setCsr(contract.getBase64Csr())
				.setCertificateType(Enum.valueOf(CertificateTypeType.class, contract.getCertificate().getType().name()))
				.setValidityPeriodMonths(contract.getValidityPeriod())
				.setDescription(contract.getDescription());
	}
}
