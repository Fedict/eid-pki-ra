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
package be.fedict.eid.pkira.mocks;

import javax.jws.HandlerChain;
import javax.jws.WebService;

import oasis.names.tc.dss._1_0.core.schema.AnyType;
import oasis.names.tc.dss._1_0.core.schema.ResponseBaseType;
import oasis.names.tc.dss._1_0.core.schema.Result;
import oasis.names.tc.dss._1_0.core.schema.VerifyRequest;
import oasis.names.tc.saml._1_0.assertion.NameIdentifierType;
import be.fedict.eid.dss.ws.DigitalSignatureServiceConstants;
import be.fedict.eid.dss.ws.DigitalSignatureServicePortType;

/**
 * @author Jan Van den Bergh
 */
@WebService(endpointInterface = "be.fedict.eid.dss.ws.DigitalSignatureServicePortType")
@HandlerChain(file="HandlerChain.xml")
public class DigitalSignatureServiceMock implements DigitalSignatureServicePortType {

	/*
	 * (non-Javadoc)
	 * @see
	 * be.fedict.eid.dss.ws.DigitalSignatureServicePortType#verify(oasis.names
	 * .tc.dss._1_0.core.schema.VerifyRequest)
	 */
	@Override
	public ResponseBaseType verify(VerifyRequest request) {
		oasis.names.tc.dss._1_0.core.schema.ObjectFactory dssObjectFactory = new oasis.names.tc.dss._1_0.core.schema.ObjectFactory();
		oasis.names.tc.saml._1_0.assertion.ObjectFactory samlObjectFactory = new oasis.names.tc.saml._1_0.assertion.ObjectFactory(); 

		ResponseBaseType response = dssObjectFactory.createResponseBaseType();
		response.setRequestID(request.getRequestID());

		Result result = dssObjectFactory.createResult();
		result.setResultMajor(DigitalSignatureServiceConstants.RESULT_MAJOR_SUCCESS);
		result.setResultMinor(DigitalSignatureServiceConstants.RESULT_MINOR_VALID_SIGNATURE);
		response.setResult(result);

		NameIdentifierType nameIdentifier = samlObjectFactory.createNameIdentifierType();
		nameIdentifier.setValue("74081447338");		
		AnyType any = dssObjectFactory.createAnyType();
		any.getAny().add(samlObjectFactory.createNameIdentifier(nameIdentifier));
		response.setOptionalOutputs(any);

		return response;
	}

}
