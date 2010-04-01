package be.fedict.eid.pkira.portal.ra.certificaterevocation;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

import be.fedict.eid.pkira.contracts.XmlMarshallingException;
import be.fedict.eid.pkira.generated.contracts.CertificateRevocationResponseType;
import be.fedict.eid.pkira.portal.ra.AbstractSignatureHttpRequestHandler;

@Scope(ScopeType.APPLICATION)
@Name(RevocationContractDssSignatureHttpRequestHandler.NAME)
@BypassInterceptors
public class RevocationContractDssSignatureHttpRequestHandler 
		extends AbstractSignatureHttpRequestHandler<CertificateRevocationResponseType> {

	public static final String NAME = "be.fedict.eid.pkira.RevocationContractHandler";
	
	@Override
	protected String getErrorPage() {
		return "/page/contract/dssSignError.seam";
	}

	@Override
	protected String getNextPage() {
		return "/page/certificates/certificates_list.seam";
	}

	@Override
	protected String invokeServiceClient(String contract) throws Exception {
		return getServiceClient().revokeCertificate(contract);
	}

	@Override
	public String getResourcePath() {
		return "/revocationContractHandler";
	}

	@Override
	protected CertificateRevocationResponseType unmarshall(String result) throws XmlMarshallingException {
		return getContractsClient().unmarshal(result, CertificateRevocationResponseType.class);
	}

}
