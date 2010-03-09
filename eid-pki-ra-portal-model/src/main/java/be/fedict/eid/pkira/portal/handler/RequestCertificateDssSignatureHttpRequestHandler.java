package be.fedict.eid.pkira.portal.handler;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

import be.fedict.eid.pkira.contracts.XmlMarshallingException;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningResponseType;

@Scope(ScopeType.APPLICATION)
@Name("requestCertificateResource")
@BypassInterceptors
public class RequestCertificateDssSignatureHttpRequestHandler 
		extends AbstractDssSignatureHttpRequestHandler<CertificateSigningResponseType> {

	@Override
	protected String getErrorPage() {
		return "/page/csr/dssSignError.seam";
	}

	@Override
	protected String getNextPage() {
		return "/page/csr/dssSignSucces.seam";
	}

	@Override
	protected String invokeServiceClient(String contract) throws Exception {
		return getServiceClient().signCertificate(contract);
	}

	@Override
	public String getResourcePath() {
		return "/requestCertificateResource";
	}

	@Override
	protected CertificateSigningResponseType unmarshall(String result) throws XmlMarshallingException {
		return getContractsClient().unmarshal(result, CertificateSigningResponseType.class);
	}

}
