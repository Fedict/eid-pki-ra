package be.fedict.eid.pkira.blm.model.contracthandler.services;

import javax.ejb.Local;

import be.fedict.eid.pkira.blm.model.contracthandler.ContractHandlerBeanException;
import be.fedict.eid.pkira.blm.model.contracts.CertificateRevocationContract;
import be.fedict.eid.pkira.blm.model.contracts.CertificateSigningContract;

@Local
public interface XKMSService {
	
	String NAME = "be.fedict.eid.pkira.blm.xkmsService";

	/**
	 * Let the CA sign the CSR. If this fails, the error will be logged here.
	 * @param csr The CSR to sign in PEM format.
	 * @return the signed certificate in PEM format. 
	 * @exception ContractHandlerBeanException when an error occurred calling the back-end.
	 */
	public String sign(CertificateSigningContract contract) throws ContractHandlerBeanException;
	
	/**
	 * Revoke a certificate, returning if this was succesful.
	 * @exception ContractHandlerBeanException when an error occurred calling the back-end.
	 */
	public void revoke(CertificateRevocationContract contract) throws ContractHandlerBeanException;
}
