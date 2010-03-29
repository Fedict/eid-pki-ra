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

package be.fedict.eid.pkira.portal.ra.certificaterequest;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.pkira.contracts.CertificateSigningRequestBuilder;
import be.fedict.eid.pkira.contracts.XmlMarshallingException;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningRequestType;
import be.fedict.eid.pkira.generated.contracts.CertificateTypeType;
import be.fedict.eid.pkira.portal.ra.AbstractContract;
import be.fedict.eid.pkira.portal.ra.AbstractPreSigningHandler;

/**
 * @author Bram Baeyens
 */
@Name(RequestContractPreSigningHandlerBean.NAME)
@Scope(ScopeType.EVENT)
public class RequestContractPreSigningHandlerBean extends AbstractPreSigningHandler<RequestContractSigningWrapper> {

	public static final String NAME = "be.fedict.eid.pkira.portal.requestContractPreSigningHandler";
	
	@In(create = true, value = RequestContractSigningWrapper.NAME)
	@Out(value = RequestContractSigningWrapper.NAME)
	private RequestContractSigningWrapper signingWrapper;

	@Override
	public void setSigningWrapper(RequestContractSigningWrapper signingWrapper) {
		this.signingWrapper = signingWrapper;
	}

	private CertificateSigningRequestBuilder initBuilder(RequestContract contract) {
		return new CertificateSigningRequestBuilder()
				.setOperator(initBuilder(contract.getOperator()).toEntityType())
				.setLegalNotice(contract.getLegalNotice())
				.setDistinguishedName(contract.getDistinguishedName())
				.setCsr(contract.getBase64Csr())
				.setCertificateType(Enum.valueOf(CertificateTypeType.class, contract.getCertificateType().name()))
				.setValidityPeriodMonths(contract.getValidityPeriod()).setDescription(contract.getDescription());
	}

	@Override
	protected RequestContractSigningWrapper getSigningWrapper() {
		return signingWrapper;
	}

	@Override
	protected String marshalBase64CsrXml(AbstractContract contract) throws XmlMarshallingException {
		CertificateSigningRequestBuilder builder = initBuilder((RequestContract) contract);
		CertificateSigningRequestType requestType = builder.toRequestType();
		String base64Xml = getContractsClientPortal().marshalToBase64(requestType, CertificateSigningRequestType.class);
		return base64Xml;
	}
}
