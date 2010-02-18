package be.fedict.eid.blm.model;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
public class ContractHandlerBean implements ContractHandler {

	/* (non-Javadoc)
	 * @see be.fedict.eid.blm.model.ContractHandler#signCertificate(java.lang.String)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public String signCertificate(String request) {
		return "Not supported by bean";
	}
	
	/* (non-Javadoc)
	 * @see be.fedict.eid.blm.model.ContractHandler#revokeCertificate(java.lang.String)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public String revokeCertificate(String request) {
		return "Not supported by bean";
	}
}
