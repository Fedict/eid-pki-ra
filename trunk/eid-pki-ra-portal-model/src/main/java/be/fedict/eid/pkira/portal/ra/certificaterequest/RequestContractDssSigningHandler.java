package be.fedict.eid.pkira.portal.ra.certificaterequest;

import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.contracts.XmlMarshallingException;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningResponseType;
import be.fedict.eid.pkira.portal.ra.AbstractDssSigningHandler;

@Name(RequestContractDssSigningHandler.NAME)
public class RequestContractDssSigningHandler extends AbstractDssSigningHandler<CertificateSigningResponseType> {

	public static final String NAME = "be.fedict.eid.pkira.portal.requestContractDssSigningHandler";

	@Override
	protected String invokeServiceClient(String contract) throws Exception {
		return getServiceClient().signCertificate(contract);
	}

	@Override
	protected CertificateSigningResponseType unmarshall(String result) throws XmlMarshallingException {
		return getContractsClient().unmarshal(result, CertificateSigningResponseType.class);
	}
}
