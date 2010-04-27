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

package be.fedict.eid.pkira.portal.contract;

import java.io.Serializable;

import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.generated.privatews.ContractWS;
import be.fedict.eid.pkira.portal.ra.CertificateType;

/**
 * @author Bram Baeyens
 *
 */
@Name(ContractMapper.NAME)
public class ContractMapper implements Serializable {

	private static final long serialVersionUID = 7720168050778002320L;
	
	public static final String NAME = "be.fedict.eid.pkira.portal.contractMapper";

	public Contract map(ContractWS contractWS) {
		Contract contract = new Contract();
		contract.setId(contractWS.getContractId());
		contract.setCertificateDomainName(contractWS.getCertificateDomainName());
		contract.setCertificateType(Enum.valueOf(CertificateType.class, contractWS.getCertificateType().name()));
		contract.setContractType(Enum.valueOf(ContractType.class, contractWS.getContractType().name()));
		contract.setCreationDate(contractWS.getCreationDate().toGregorianCalendar().getTime());
		contract.setDnExpression(contractWS.getDnExpression());
		contract.setHasCertificate(contractWS.isHasCertificate());
		contract.setRequesterName(contractWS.getRequesterName());		
		return contract;
	}
	
	

}
