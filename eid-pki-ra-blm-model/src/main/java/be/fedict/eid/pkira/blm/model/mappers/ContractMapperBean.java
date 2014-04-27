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

package be.fedict.eid.pkira.blm.model.mappers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.pkira.blm.model.contracts.AbstractContract;
import be.fedict.eid.pkira.blm.model.contracts.CertificateRevocationContract;
import be.fedict.eid.pkira.blm.model.contracts.CertificateSigningContract;
import be.fedict.eid.pkira.generated.privatews.CertificateTypeWS;
import be.fedict.eid.pkira.generated.privatews.ContractTypeWS;
import be.fedict.eid.pkira.generated.privatews.ContractWS;
import be.fedict.eid.pkira.generated.privatews.ObjectFactory;

import static be.fedict.eid.pkira.contracts.util.JAXBUtil.createXmlGregorianCalendar;

/**
 * @author Bram Baeyens
 *
 */
@Name(ContractMapper.NAME)
@Scope(ScopeType.STATELESS)
public class ContractMapperBean implements Serializable, ContractMapper {

	private static final long serialVersionUID = 518966394376372668L;
	
	private final ObjectFactory factory;
	
	public ContractMapperBean() {
		this.factory = new ObjectFactory();
	}

	@Override
	public Collection<ContractWS> map(List<AbstractContract> contracts) {
		List<ContractWS> contractWSList = new ArrayList<ContractWS>();
		for (AbstractContract contract : contracts) {
			contractWSList.add(map(contract));
		}
		return contractWSList;
	}

    @Override
	public ContractWS map(AbstractContract contract) {
		ContractWS contractWS = factory.createContractWS();
		contractWS.setContractId(contract.getId());
		contractWS.setCreationDate(createXmlGregorianCalendar(contract.getCreationDate()));
		contractWS.setDnExpression(contract.getSubject());
		contractWS.setRequesterName(contract.getRequester());
		contractWS.setCertificateDomainName(contract.getCertificateDomain().getName());
		contractWS.setContractType(deriveContractType(contract));
		contractWS.setCertificateType(getCertificateType(contract));
		contractWS.setCertificateId(getCertificateId(contract)); 
		contractWS.setResult(contract.getResult()!=null ? contract.getResult().value() : null);
		contractWS.setResultMessage(contract.getResultMessage());
		return contractWS;
	}

	private ContractTypeWS deriveContractType(AbstractContract contract) {
		if (contract instanceof CertificateRevocationContract) {
			return ContractTypeWS.REVOCATION;
		} else if (contract instanceof CertificateSigningContract) {
			return ContractTypeWS.SIGNING;
		}
		return null;
	}

	private CertificateTypeWS getCertificateType(AbstractContract contract) {
		if (contract instanceof CertificateSigningContract) {
			return Enum.valueOf(CertificateTypeWS.class, 
					((CertificateSigningContract) contract).getCertificateType().name());
		}
		return null;
	}

	private Integer getCertificateId(AbstractContract contract) {
		if (contract instanceof CertificateSigningContract) {
			CertificateSigningContract certificateSigningContract = (CertificateSigningContract) contract;
			return certificateSigningContract.getCertificate() != null ? certificateSigningContract.getCertificate().getId() : null;
		}
		return null;
	}

}
