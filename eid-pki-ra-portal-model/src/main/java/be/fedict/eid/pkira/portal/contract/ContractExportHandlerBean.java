
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
package be.fedict.eid.pkira.portal.contract;

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.pkira.portal.util.AbstractCsvCreatorBean;

@Name(ContractExportHandler.NAME)
@Scope(ScopeType.CONVERSATION)
public class ContractExportHandlerBean extends AbstractCsvCreatorBean implements ContractExportHandler {

    @In(value = ContractWSQuery.NAME, create = true)
    private ContractWSQuery contractWSQuery;

    @Override
    public void exportToCSV() {
        List<Contract> contracts = contractWSQuery.getFindContracts();
        CsvBuilder csv = new CsvBuilder();

        // add column headers
        csv
            .append(messages.get("contract.type"))
            .append(messages.get("contract.requestername"))
            .append(messages.get("certificate.type"))
            .append(messages.get("contract.dnexpression"))
            .append(messages.get("contract.creationdate"))
            .append(messages.get("contract.result"))
            .append(messages.get("contract.resultMessage"))
            .nextRow();

        // add rows
        for (Contract contract : contracts) {
            csv
                .append(translateContractType(contract.getContractType()))
                .append(contract.getRequesterName())
                    .append(translateCertificateType(contract.getCertificateType()))
                    .append(contract.getDnExpression())
                    .append(contract.getCreationDate())
                    .append(contract.getResult())
                    .append(contract.getResultMessage())
                    .nextRow();
        }

        csv.download("pkira-contract-list.csv", "text/csv");
    }


}
