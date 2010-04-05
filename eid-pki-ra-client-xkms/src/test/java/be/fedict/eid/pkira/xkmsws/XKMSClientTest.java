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

package be.fedict.eid.pkira.xkmsws;

import java.net.ServerSocket;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Holder;

import org.junit.Test;
import org.w3._2000._09.xmldsig_.SignatureType;
import org.w3._2002._03.xkms_xbulk.BulkRegisterType.SignedPart;
import org.w3._2002._03.xkms_xbulk_wsdl.XKMSPortType;

import be.fedict.eid.pkira.xkmsws.XKMSClient;

public class XKMSClientTest {

	@WebService(endpointInterface = "org.w3._2002._03.xkms_xbulk_wsdl.XKMSPortType")
	public static class XKMSTestPort implements XKMSPortType {

		@Override
		public void bulkRegister(
				SignedPart signedPart,
				Holder<SignatureType> signature,
				Holder<org.w3._2002._03.xkms_xbulk.BulkRegisterResultType.SignedPart> signedPart0) {
		}
	}

	private static int getFreePort() throws Exception {
		ServerSocket serverSocket = new ServerSocket(0);
		int port = serverSocket.getLocalPort();
		serverSocket.close();
		return port;
	}

	@Test
	public void testCreateCertificate() throws Exception {
		Endpoint endpoint = Endpoint.create(new XKMSTestPort());
		int port = getFreePort();
		String url = "http://localhost:" + port + "/xkms";
		endpoint.publish(url);

		XKMSClient xkmsClient = new XKMSClient(url);
		xkmsClient.createCertificate("hello world".getBytes());

		endpoint.stop();
	}
}
