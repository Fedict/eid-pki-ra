package be.fedict.eid.pkira.ws.impl;

import javax.jws.WebService;

import be.fedict.eid.pkira.blm.ws.EIDPKIRAPortType;

@WebService(endpointInterface="be.fedict.eid.pkira.blm.ws.EIDPKIRAPortType")
public class EIDPKIRAServiceImpl implements EIDPKIRAPortType {

	@Override
	public String revokeCertificate(String request) {
		return "Not implemented";
	}

	@Override
	public String signCertificate(String request) {
		return "Not implemented";
	}

}
