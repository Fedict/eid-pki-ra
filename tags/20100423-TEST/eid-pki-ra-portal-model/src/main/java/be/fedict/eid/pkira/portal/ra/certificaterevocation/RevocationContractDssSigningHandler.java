package be.fedict.eid.pkira.portal.ra.certificaterevocation;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.pkira.contracts.XmlMarshallingException;
import be.fedict.eid.pkira.generated.contracts.CertificateRevocationResponseType;
import be.fedict.eid.pkira.portal.ra.AbstractDssSigningHandler;

@Scope(ScopeType.APPLICATION)
@Name(RevocationContractDssSigningHandler.NAME)
public class RevocationContractDssSigningHandler 	extends AbstractDssSigningHandler<CertificateRevocationResponseType> {

	public static final String NAME = "be.fedict.eid.pkira.revocationContractDssSigningHandler";

	@Override
	protected String invokeServiceClient(String contract) throws Exception {
		return getServiceClient().revokeCertificate(contract);
	}

	@Override
	protected CertificateRevocationResponseType unmarshall(String result) throws XmlMarshallingException {
		return getContractsClient().unmarshal(result, CertificateRevocationResponseType.class);
	}

}
