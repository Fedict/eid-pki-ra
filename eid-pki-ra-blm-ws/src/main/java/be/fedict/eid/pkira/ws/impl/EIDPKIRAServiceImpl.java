package be.fedict.eid.pkira.ws.impl;

import javax.ejb.EJB;
import javax.jws.WebService;

import be.fedict.eid.blm.model.ContractHandler;
import be.fedict.eid.pkira.blm.ws.EIDPKIRAPortType;

@WebService(endpointInterface="be.fedict.eid.pkira.blm.ws.EIDPKIRAPortType")
public class EIDPKIRAServiceImpl implements EIDPKIRAPortType {

	@EJB
	private ContractHandler contractHandler;
	
	@Override
	public String revokeCertificate(String request) {
		return contractHandler.revokeCertificate(request);
	}

	@Override
	public String signCertificate(String request) {
		return contractHandler.signCertificate(request);
	}

}
