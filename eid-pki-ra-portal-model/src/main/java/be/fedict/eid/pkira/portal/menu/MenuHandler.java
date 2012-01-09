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

package be.fedict.eid.pkira.portal.menu;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.pkira.common.security.EIdUserCredentials;
import be.fedict.eid.pkira.privatews.EIDPKIRAPrivateServiceClient;

@Name(MenuHandler.NAME)
@Scope(ScopeType.SESSION)
public class MenuHandler implements Serializable {

	private static final long serialVersionUID = 3778075742509845834L;

	public final static String NAME = "be.fedict.eid.pkira.portal.menuHandler";
	// id of the selected Item
	private String selectedItem = "home";

	@In(value = EIDPKIRAPrivateServiceClient.NAME, create = true)
	private EIDPKIRAPrivateServiceClient eidpkiraPrivateServiceClient;

	@In
	private EIdUserCredentials credentials;

	public void selectItem(String item) {
		this.selectedItem = item;
	}

	@Observer("org.jboss.seam.localeSelected")
	public void localeChanged(String locale) {
		if (credentials!=null) {
			eidpkiraPrivateServiceClient.changeLocale(credentials.getUser().getRRN(), locale);
		}
	}

	public boolean isSelected(String item) {
		if (selectedItem.equals(item)) {
			return true;
		}
		return false;
	}

	public String styleIfSelected(String item) {
		if (isSelected(item) == true) {
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

	public void setEidpkiraPrivateServiceClient(EIDPKIRAPrivateServiceClient eidpkiraPrivateServiceClient) {
		this.eidpkiraPrivateServiceClient = eidpkiraPrivateServiceClient;
	}

	public void setCredentials(EIdUserCredentials credentials) {
		this.credentials = credentials;
	}

}
