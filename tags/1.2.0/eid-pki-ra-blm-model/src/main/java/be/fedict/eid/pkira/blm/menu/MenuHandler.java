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

package be.fedict.eid.pkira.blm.menu;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name(MenuHandler.NAME)
@Scope(ScopeType.SESSION)
public class MenuHandler implements Serializable {
	
	private static final long serialVersionUID = -9215851254156584501L;
	
	public final static String NAME="be.fedict.eid.pkira.blm.menuHandler";
	
	//id of the selected Item
	private String selectedItem = "home";

	public void selectItem(String item){
		this.selectedItem = item;
	}
	
	public boolean isSelected(String item){
        return selectedItem.equals(item);
    }
	
	public String styleIfSelected(String item){
		if(isSelected(item)){
			return "itemSelected";
		}
		return "itemNotSelected";
	}
	
	public void setSelectedItem(String selectedItem) {
		this.selectedItem = selectedItem;
	}

	public String getSelectedItem() {
		return selectedItem;
	}

    public String openHomePage() {
        return "/home.xhtml";
    }

    public String openCertificateAuthoritiesPage() {
        return "/page/certificateauthority/certificateauthority-list.xhtml";
    }

    public String openCertificateDomainsPage() {
        return "/page/certificatedomain/certificatedomain-list.xhtml";
    }

    public String openUsersPage() {
        return "/page/user/user-list.xhtml";
    }

    public String openRegistrationsPage() {
        return "/page/registration/bulkapproval.xhtml";
    }

    public String openBlacklistPage() {
        return "/page/blacklist/blacklistitem-list.xhtml";
    }

    public String openCertificatesPage() {
        return "/page/certificates/certificate-list.xhtml";
    }

    public String openContractsPage() {
        return "/page/contracts/contract-list.xhtml";
    }

    public String openConfigurationPage() {
        return "/page/configurationentry/configurationentry-list.xhtml";
    }

    public String openReportGenerationPage() {
        return "/page/report/report-generation.xhtml";
    }

    public String openReportEntryListPage() {
        return "/page/report/reportentry-list.xhtml";
    }

    public String openStatisticsPage() {
        return "/page/stats/statistics.xhtml";
    }

    public String openXkmsLogListPage() {
        return "/page/xkmslog/xkms-log-list.xhtml";
    }

    public String openLoginPage() {
        return "/login.xhtml";
    }
}
