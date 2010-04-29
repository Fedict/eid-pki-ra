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

package be.fedict.eid.pkira.portal.ra;

import java.io.Serializable;

import org.jboss.seam.annotations.In;

import be.fedict.eid.pkira.privatews.EIDPKIRAPrivateServiceClient;

/**
 * @author Bram Baeyens
 *
 */
public abstract class PrivateWSQuery implements Serializable {
	
	private static final long serialVersionUID = 8675955887714887342L;
	
	@In(value = EIDPKIRAPrivateServiceClient.NAME, create = true)
	private EIDPKIRAPrivateServiceClient serviceClient;

	public EIDPKIRAPrivateServiceClient getServiceClient() {
		return serviceClient;
	}
}
