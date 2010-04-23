/*
 * eID PKI RA Project.
 * Copyright (C) 2010 FedICT.
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

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * Backing bean for the reporting.
 * 
 * @author Jan Van den Bergh
 */
@Name(ReportConfiguration.NAME)
@Scope(ScopeType.CONVERSATION)
public class ReportConfiguration implements Serializable {

	private static final long serialVersionUID = -8148256572976871989L;

	public static final String NAME = "be.fedict.eid.pkira.blm.reportConfiguration";

	@In(value = ReportManagerBean.NAME, create = true)
	private ReportManager reportManager;

	@In(value = "#{facesContext.externalContext}")
	private ExternalContext externalContext;

	@In(value = "#{facesContext}")
	private FacesContext facesContext;

	private String month;
	private boolean includeCertificateAuthorityReport = true;
	private boolean includeCertificateDomainReport = true;

	public ReportConfiguration() {
		System.out.println("test");
	}

	public void generateReport() {
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
		response.setContentType("test/xml");
		response.addHeader("Content-disposition", "attachment; filename=\"" + month + ".xml\"");

		String report = reportManager.generateReport(month, includeCertificateAuthorityReport,
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

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
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
}
