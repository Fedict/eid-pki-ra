package be.fedict.eid.pkira.blm.model.contracthandler.services;

import javax.ejb.Local;

import be.fedict.eid.pkira.blm.model.contracthandler.ContractHandlerBeanException;
import be.fedict.eid.pkira.blm.model.contracts.AbstractContract;
import be.fedict.eid.pkira.blm.model.contracts.CertificateSigningContract;
import be.fedict.eid.pkira.blm.model.contracts.CertificateType;

@Local
public interface XKMSService {

	String NAME = "be.fedict.eid.pkira.blm.xkmsService";

	/**
	 * Let the CA sign the CSR. If this fails, the error will be logged here.
	 * @param csr TODO
	 * @param csr
	 *            The CSR to sign in PEM format.
	 * 
	 * @return the signed certificate in PEM format.
	 * @exception ContractHandlerBeanException
	 *                when an error occurred calling the back-end.
	 */
	public String sign(CertificateSigningContract contract, String csr) throws ContractHandlerBeanException;

	/**
	 * Revoke a certificate, returning if this was succesful.
	 * @param certificateType
	 *            the certificate type to revoke
	 * @param certificateStr TODO
	 * 
	 * @exception ContractHandlerBeanException
	 *                when an error occurred calling the back-end.
	 */
	public void revoke(AbstractContract contract, CertificateType certificateType, String certificateStr) throws ContractHandlerBeanException;
}
