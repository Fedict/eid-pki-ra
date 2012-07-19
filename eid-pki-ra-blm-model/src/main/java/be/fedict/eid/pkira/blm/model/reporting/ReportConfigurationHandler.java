/*
 * eID PKI RA Project.
 * Copyright (C) 2010-2012 FedICT.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version
 * 3.0 as published by the Free Software Foundation.
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, see
 * http://www.gnu.org/licenses/.
 */
package be.fedict.eid.pkira.blm.model.reporting;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;

/**
 * Backing bean for the reporting.
 * 
 * @author Jan Van den Bergh
 */
@Name(ReportConfigurationHandler.NAME)
@Scope(ScopeType.CONVERSATION)
public class ReportConfigurationHandler implements Serializable {

	private static final long serialVersionUID = -8148256572976871989L;

	public static final String NAME = "be.fedict.eid.pkira.blm.reportConfigurationHandler";

	@In(value = ReportManagerBean.NAME, create = true)
	private ReportManager reportManager;

	@In(value = "#{facesContext.externalContext}")
	private ExternalContext externalContext;

	@In(value = "#{facesContext}")
	private FacesContext facesContext;
	
	@In(create=true)
	private FacesMessages facesMessages;

	private String endMonth;
	private String startMonth;
	private boolean includeCertificateAuthorityReport = true;
	private boolean includeCertificateDomainReport = true;

	public void generateReport() {
		if (StringUtils.isBlank(startMonth) || StringUtils.isBlank(endMonth)) {
			return;
		}
		
		if (startMonth.compareTo(endMonth)>0) {
			facesMessages.addFromResourceBundle(Severity.ERROR, "reports.startAfterEnd");
			return;
		}
		
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
		response.setContentType("test/xml");
		String fileName = (StringUtils.equals(startMonth,endMonth) ? startMonth : startMonth + " - " + endMonth) + ".xml\"";
		response.addHeader("Content-disposition", "attachment; filename=\"" + fileName);

		String report = reportManager.generateReport(startMonth, endMonth, includeCertificateAuthorityReport,
				includeCertificateDomainReport);

		try {
			ServletOutputStream servletOutputStream = response.getOutputStream();
			servletOutputStream.write(report.getBytes());
			servletOutputStream.flush();
			servletOutputStream.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		facesContext.responseComplete();
	}

	public boolean isIncludeCertificateAuthorityReport() {
		return includeCertificateAuthorityReport;
	}

	public void setIncludeCertificateAuthorityReport(boolean includeCertificateAuthorityReport) {
		this.includeCertificateAuthorityReport = includeCertificateAuthorityReport;
	}

	public boolean isIncludeCertificateDomainReport() {
		return includeCertificateDomainReport;
	}

	public void setIncludeCertificateDomainReport(boolean includeCertificateDomainReport) {
		this.includeCertificateDomainReport = includeCertificateDomainReport;
	}

	public String getEndMonth() {
		return endMonth;
	}

	public void setEndMonth(String endMonth) {
		this.endMonth = endMonth;
	}

	public String getStartMonth() {
		return startMonth;
	}

	public void setStartMonth(String startMonth) {
		this.startMonth = startMonth;
	}
}
