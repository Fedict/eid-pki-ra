package be.fedict.eid.pkira.portal.handler;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

import be.fedict.eid.pkira.contracts.XmlMarshallingException;
import be.fedict.eid.pkira.generated.contracts.CertificateRevocationResponseType;

@Scope(ScopeType.APPLICATION)
@Name("revocationContractHandler")
@BypassInterceptors
public class RevocationContractDssSignatureHttpRequestHandler 
		extends AbstractDssSignatureHttpRequestHandler<CertificateRevocationResponseType> {

	@Override
	protected String getErrorPage() {
		return "/page/contract/dssSignError.seam";
	}

	@Override
	protected String getNextPage() {
		return "/page/listcertificates/listcertificates.seam";
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
