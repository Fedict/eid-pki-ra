package be.fedict.eid.pkira.portal.ra.certificaterequest;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

import be.fedict.eid.pkira.contracts.XmlMarshallingException;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningResponseType;
import be.fedict.eid.pkira.portal.ra.AbstractSignatureHttpRequestHandler;

@Scope(ScopeType.APPLICATION)
@Name(RequestContractSignatureHttpRequestHandler.NAME)
@BypassInterceptors
public class RequestContractSignatureHttpRequestHandler 
		extends AbstractSignatureHttpRequestHandler<CertificateSigningResponseType> {

	public static final String NAME = "be.fedict.eid.pkira.portal.requestContractHandler";
	
	@Override
	protected String getErrorPage() {
		return "/page/contract/dssSignError.seam";
	}

	@Override
	protected String getNextPage() {
		return "/page/contract/dssSignSucces.seam";
	}

	@Override
	protected String invokeServiceClient(String contract) throws Exception {
		return getServiceClient().signCertificate(contract);
	}

	@Override
	public String getResourcePath() {
		return "/requestContractHandler";
	}

	@Override
	protected CertificateSigningResponseType unmarshall(String result) throws XmlMarshallingException {
		return getContractsClient().unmarshal(result, CertificateSigningResponseType.class);
	}
}
