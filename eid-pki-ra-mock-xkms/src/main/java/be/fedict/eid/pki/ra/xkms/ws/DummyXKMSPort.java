/*
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

package be.fedict.eid.pki.ra.xkms.ws;

import javax.jws.WebService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3._2002._03.xkms_xbulk.BulkRegisterResultType;
import org.w3._2002._03.xkms_xbulk.BulkRegisterType;
import org.w3._2002._03.xkms_xbulk_wsdl.XKMSPortType;

@WebService(endpointInterface = "org.w3._2002._03.xkms_xbulk_wsdl.XKMSPortType")
public class DummyXKMSPort implements XKMSPortType {

	private static final Log LOG = LogFactory.getLog(DummyXKMSPort.class);

	/* (non-Javadoc)
	 * @see org.w3._2002._03.xkms_xbulk_wsdl.XKMSPortType#bulkRegister(org.w3._2002._03.xkms_xbulk.BulkRegisterType)
	 */
	@Override
	public BulkRegisterResultType bulkRegister(BulkRegisterType bulkRegister) {		
		LOG.debug("bulkRegister");
		
		// TODO Auto-generated method stub
		return null;
	}


}
