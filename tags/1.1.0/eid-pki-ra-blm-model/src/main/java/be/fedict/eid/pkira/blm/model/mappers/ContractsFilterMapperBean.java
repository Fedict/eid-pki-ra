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

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.pkira.blm.model.contracts.CertificateType;
import be.fedict.eid.pkira.blm.model.contracts.ContractsFilter;
import be.fedict.eid.pkira.generated.privatews.ContractTypeWS;
import be.fedict.eid.pkira.generated.privatews.ContractsFilterWS;

/**
 * @author Bram Baeyens
 *
 */
@Name(ContractsFilterMapper.NAME)
@Scope(ScopeType.STATELESS)
public class ContractsFilterMapperBean implements Serializable, ContractsFilterMapper {

	private static final long serialVersionUID = 518966394376372668L;

    @Override
    public ContractsFilter mapContractsFilter(ContractsFilterWS contractsFilterWS) {
        ContractsFilter contractsFilter = new ContractsFilter();
        if (contractsFilterWS !=null) {
            contractsFilter.setCertificateDomainId(contractsFilterWS.getCertificateDomainId());
            contractsFilter.setIncludeRequestContracts(contractsFilterWS.getContractType() != ContractTypeWS.REVOCATION);
            contractsFilter.setIncludeRevocationContracts(contractsFilterWS.getContractType() != ContractTypeWS.SIGNING);
            if (contractsFilterWS.getCertificateType()!=null) contractsFilter.setCertificateType(CertificateType.valueOf(contractsFilterWS.getCertificateType().name()));
            contractsFilter.setRequesterName(contractsFilterWS.getRequesterName());
            contractsFilter.setDnExpression(contractsFilterWS.getDnExpression());
            if (contractsFilterWS.getCreationDateFrom()!=null) contractsFilter.setCreationDateFrom(contractsFilterWS.getCreationDateFrom().toGregorianCalendar().getTime());
            if (contractsFilterWS.getCreationDateTo()!=null) contractsFilter.setCreationDateTo(contractsFilterWS.getCreationDateTo().toGregorianCalendar().getTime());
            contractsFilter.setResultMessage(contractsFilterWS.getResultMessage());
        }
        return contractsFilter;
    }

}
