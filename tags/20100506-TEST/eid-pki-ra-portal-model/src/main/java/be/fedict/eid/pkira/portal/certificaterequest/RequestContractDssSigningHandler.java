package be.fedict.eid.pkira.portal.certificaterequest;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.contracts.XmlMarshallingException;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningResponseType;
import be.fedict.eid.pkira.portal.certificate.CertificateWSHome;
import be.fedict.eid.pkira.portal.signing.AbstractDssSigningHandler;

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

	@Override
	protected String handleRedirect(String redirectStatus,
			CertificateSigningResponseType serviceClientResponse) {		
		if(redirectStatus == null){
			return "error";
		}
		if(serviceClientResponse == null){
			return redirectStatus;
		}
		
		getCertificateWSHome().setId(serviceClientResponse.getCertificateID());
		
		return redirectStatus;
	}

	public CertificateWSHome getCertificateWSHome() {
		return (CertificateWSHome) Component.getInstance(CertificateWSHome.NAME);
	}
}
