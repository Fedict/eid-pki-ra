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
 */e.fedict.eid.pkira.portal.contract;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.common.security.EIdUserCredentials;
import be.fedict.eid.pkira.common.util.StringShortener;
import be.fedict.eid.pkira.generated.privatews.ContractWS;
import be.fedict.eid.pkira.portal.certificatedomain.CertificateDomainWSHome;
import be.fedict.eid.pkira.portal.framework.DataTableWSQuery;

/**
 * @author Bram Baeyens
 *
 */
@Name(ContractWSQuery.NAME)
public class ContractWSQuery extends DataTableWSQuery {

	private static final long serialVersionUID = 3439133481861208655L;
	
	public static final String NAME = "be.fedict.eid.pkira.portal.contractWSQuery";
	
	@In
	private EIdUserCredentials credentials;
	
	@In(value=ContractMapper.NAME, create=true)
	private ContractMapper contractMapper;
	
	@In(value=CertificateDomainWSHome.NAME, create=true)
	private CertificateDomainWSHome certificateDomainWSHome;

	private Integer certificateDomainId;

	private List<Contract> resultList;
	
	public List<Contract> getFindContracts() {
		if (resultList == null) {
			resultList = new ArrayList<Contract>();
			for (ContractWS contractWS : getServiceClient().findContracts(certificateDomainId, credentials.getUser().getRRN())) {
				resultList.add(contractMapper.map(contractWS));
			}
		}
		return resultList;
	}
	
	public Integer getCertificateDomainId() {
		return certificateDomainId;
	}

	public void setCertificateDomainId(Integer certificateDomainId) {
		resultList = null;
		this.certificateDomainId = certificateDomainId;
	}
	
	public CertificateDomainWSHome getCertificateDomainWSHome() {
		if (certificateDomainId != null) {
			certificateDomainWSHome.setId(certificateDomainId);		
		}
		return certificateDomainWSHome;
	}

    public String getCertificateDomainExpressionShortened() {
        return StringShortener.shorten(getCertificateDomainExpression(), 120);
    }

    public String getCertificateDomainExpression() {
        return getCertificateDomainWSHome().getInstance().getDnExpression();
    }

}
