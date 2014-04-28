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
package be.fedict.eid.pkira.portal.certificate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.log.Log;

import be.fedict.eid.pkira.common.security.EIdUserCredentials;
import be.fedict.eid.pkira.common.util.FilterHelper;
import be.fedict.eid.pkira.common.util.StringShortener;
import be.fedict.eid.pkira.generated.privatews.CertificateWS;
import be.fedict.eid.pkira.portal.certificatedomain.CertificateDomainWSHome;
import be.fedict.eid.pkira.portal.signing.AbstractDssSigningHandler;
import be.fedict.eid.pkira.portal.util.TypeMapper;
import be.fedict.eid.pkira.privatews.EIDPKIRAPrivateServiceClient;

@Name(CertificateHandler.NAME)
@Scope(ScopeType.CONVERSATION)
public class CertificateHandlerBean implements CertificateHandler {

	private static final long serialVersionUID = -5017092109045531172L;

	private static final String CERTIFICATES_NAME = "certificates";

	@Out(value = Certificate.NAME, scope = ScopeType.CONVERSATION, required = false)
	private Certificate certificate;

    @In(EIdUserCredentials.NAME)
	private EIdUserCredentials credentials;

	@In(value = EIDPKIRAPrivateServiceClient.NAME, create = true)
	private EIDPKIRAPrivateServiceClient eidpkiraPrivateServiceClient;

	@Logger
	private Log log;

	@DataModel(scope=ScopeType.PAGE)
	private List<Certificate> certificates;

	@In(value = TypeMapper.NAME, create = true)
	private TypeMapper typeMapper;

	private String certificateDomainWSID;

	@In(value = CertificateWSHome.NAME, create = true)
	private CertificateWSHome certificateWSHome;

	@In(value = CertificateDomainWSHome.NAME, create = true)
	private CertificateDomainWSHome certificateDomainWSHome;

	@In(value = FilterHelper.NAME, create = true)
	private FilterHelper filterHelper;

	private String dnFilterValue;
	private Date startDateFrom, startDateTo, endDateFrom, endDateTo;

	@Override
	public List<Certificate> findCertificateList() {
		return findCertificateList(credentials.getUser().getRRN());
	}

	@Override
	public List<Certificate> findCertificateList(String userRRN) {
        // TODO rewrite
		List<CertificateWS> listCertificates = Collections.emptyList();//eidpkiraPrivateServiceClient.listCertificates(userRRN, getCertificateDomainWSID());
		certificates = new ArrayList<Certificate>();
		for (CertificateWS certificatews : listCertificates) {
			certificates.add(typeMapper.map(certificatews));
		}
		return certificates;
	}

	@Factory(CERTIFICATES_NAME)
	public void initCertificateList() {
		findCertificateList(credentials.getUser().getRRN());
	}

	@Observer(AbstractDssSigningHandler.EVENT_CERTIFICATE_LIST_CHANGED)
	public void onCertificateListChanged() {
		Contexts.getConversationContext().remove(CERTIFICATES_NAME);
		Component.getInstance(CERTIFICATES_NAME);
	}

	@Override
	@Begin(join = true)
	public String prepareRevocation(Integer certificateId) {
		log.info(">>> preprareRevocation(certificateId[{0}])", certificateId);
		certificateWSHome.setId(certificateId);
		certificate = certificateWSHome.getInstance();
		log.info("<<< preprareRevocation: {0})", certificate);
		return "revokeContract";
	}

	@Override
	public boolean filterByDN(Object current) {
		if (StringUtils.isBlank(dnFilterValue)) {
			return true;
		}
		Certificate certificate = (Certificate) current;
		return filterHelper.matchWildcardExpression(dnFilterValue + "*", certificate.getDistinguishedName());
	}
	
	@Override
	public boolean filterByStartDate(Object current) {
		certificate = (Certificate) current;

		return filterHelper.filterByDate(certificate.getValidityStart(), startDateFrom, startDateTo);
	}
	
	@Override
	public boolean filterByEndDate(Object current) {
		Certificate certificate = (Certificate) current;

		return filterHelper.filterByDate(certificate.getValidityEnd(), endDateFrom, endDateTo);
	}

	protected void setEidpkiraPrivateServiceClient(EIDPKIRAPrivateServiceClient eidpkiraPrivateServiceClient) {
		this.eidpkiraPrivateServiceClient = eidpkiraPrivateServiceClient;
	}

	public void setCertificateDomainId(String certificateDomainId) {
		this.setCertificateDomainWSID(certificateDomainId);
		initCertificateList();
	}

	public CertificateDomainWSHome getCertificateDomainWSHome() {
		if (getCertificateDomainWSID() != null) {
			certificateDomainWSHome.setId(Integer.parseInt(getCertificateDomainWSID()));
		}
		return certificateDomainWSHome;
	}

	public String getCertificateDomainWSID() {
		return certificateDomainWSID;
	}

	public void setCertificateDomainWSID(String certificateDomainWSID) {
		this.certificateDomainWSID = certificateDomainWSID;
	}

	public String getDnFilterValue() {
		return dnFilterValue;
	}

	public void setDnFilterValue(String dnFilterValue) {
		this.dnFilterValue = dnFilterValue;
	}

	
	public Date getStartDateFrom() {
		return startDateFrom;
	}

	
	public void setStartDateFrom(Date startDateFrom) {
		this.startDateFrom = startDateFrom;
	}

	
	public Date getStartDateTo() {
		return startDateTo;
	}

	
	public void setStartDateTo(Date startDateTo) {
		this.startDateTo = startDateTo;
	}

	
	public Date getEndDateFrom() {
		return endDateFrom;
	}

	
	public void setEndDateFrom(Date endDateFrom) {
		this.endDateFrom = endDateFrom;
	}

	
	public Date getEndDateTo() {
		return endDateTo;
	}

	
	public void setEndDateTo(Date endDateTo) {
		this.endDateTo = endDateTo;
	}

	
	public void setFilterHelper(FilterHelper filterHelper) {
		this.filterHelper = filterHelper;
	}

    public String getCertificateDomainExpressionShortened() {
        return StringShortener.shorten(getCertificateDomainExpression(), 120);
    }

    public String getCertificateDomainExpression() {
        return getCertificateDomainWSHome().getInstance().getDnExpression();
    }

}
