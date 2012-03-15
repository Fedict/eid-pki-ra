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
package be.fedict.eid.pkira.crypto.util;

import java.security.Security;


/**
 * @author Jan Van den Bergh
 *
 */
public class BouncyCastleProviderUser {

	static {
		// Clean up old traces (in case of redeployment)
		Security.removeProvider("BC"); 

		// Make sure BC provider is known.
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); 
	}
	
	
}
