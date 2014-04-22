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

package be.fedict.eid.pkira.portal.certificaterevocation;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.contracts.XmlMarshallingException;
import be.fedict.eid.pkira.generated.contracts.CertificateRevocationResponseType;
import be.fedict.eid.pkira.portal.signing.AbstractDssSigningHandler;

@Name(RevocationContractDssSigningHandler.NAME)
public class RevocationContractDssSigningHandler extends AbstractDssSigningHandler<CertificateRevocationResponseType> {

	public static final String NAME = "be.fedict.eid.pkira.portal.revocationContractDssSigningHandler";

	@In(value = RevocationContractSigningWrapper.NAME)
	private RevocationContractSigningWrapper signingWrapper;

	@Override
	protected String invokeServiceClient(String contract) throws Exception {
		return getServiceClient().revokeCertificate(contract);
	}

	@Override
	protected CertificateRevocationResponseType unmarshall(String result) throws XmlMarshallingException {
		return getContractsClient().unmarshal(result, CertificateRevocationResponseType.class);
	}

	@Override
	protected String getTarget() {
		return signingWrapper.getDssSigningHandlerViewID();
	}

	@Override
	protected String getBase64encodedSignatureRequest() {
		return signingWrapper.getBase64CsrXml();
	}
}
