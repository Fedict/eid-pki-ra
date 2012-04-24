package be.fedict.eid.pkira.client;

import java.security.KeyStore.PrivateKeyEntry;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import be.fedict.eid.pkira.generated.contracts.CertificateRevocationResponseType;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningResponseType;
import be.fedict.eid.pkira.generated.contracts.CertificateTypeType;
import be.fedict.eid.pkira.generated.contracts.ResponseType;

public interface PKIRAClient {

	/**
	 * Creates a certificate signing request based on the information supplied.
	 * All fields are mandatory unless indicated otherwise.
	 * 
	 * @param requestId
	 *            Optional id to give to the request. PKIRA will return this
	 *            request id in its response.
	 * @param csr
	 *            certificate signing request (CSR) (in PEM format).
	 * @param certificateType
	 *            type of certificate to generate.
	 * @param operatorName
	 *            name of the operator requesting the certificate.
	 * @param operatorFunction
	 *            function of the operator requesting the certificate.
	 * @param operatorPhone
	 *            phone number of the operator requesting the certificate. This
	 *            field has to contains a valid phone number.
	 * @param validityPeriodMonths
	 *            validity period in months. Contact Fedict to the allowed
	 *            values.
	 * @param description
	 *            additional description to be added to the contract.
	 * @param legalNotice
	 *            legal notice to be added to the contract. Contact Fedict to
	 *            get the allowed value.
	 * @return the unsigned certicate signing request contract as XML document.
	 * @throws PKIRAClientException
	 *             when the contract could not be generated. This usually
	 *             indicates an error in one of the inputs.
	 */
	String createCertificateSigningRequestContract(String requestId, String csr, CertificateTypeType certificateType,
			String operatorName, String operatorFunction, String operatorPhone, int validityPeriodMonths,
			String description, String legalNotice) throws PKIRAClientException;

	/**
	 * Creates a certificate revocation request based on the information
	 * supplied. All fields are mandatory unless indicated otherwise.
	 * 
	 * @param requestId
	 *            Optional id to give to the request. PKIRA will return this
	 *            request id in its response.
	 * @param certificate
	 *            certificate to be revoked (in PEM format).
	 * @param operatorName
	 *            name of the operator requesting the certificate.
	 * @param operatorFunction
	 *            function of the operator requesting the certificate.
	 * @param operatorPhone
	 *            phone number of the operator requesting the certificate. This
	 *            field has to contains a valid phone number.
	 * @param description
	 *            additional description to be added to the contract.
	 * @param legalNotice
	 *            legal notice to be added to the contract. Contact Fedict to
	 *            get the allowed value.
	 * @return the unsigned certificate revocation request contract as XML
	 *         document.
	 * @throws PKIRAClientException
	 *             when the contract could not be generated. This usually
	 *             indicates an error in one of the inputs.
	 */
	String createCertificateRevocationRequestContract(String requestId, String certificate, String operatorName,
			String operatorFunction, String operatorPhone, String description, String legalNotice)
			throws PKIRAClientException;

	String signRequestContract(String requestContract, X509Certificate certificate, PrivateKey privateKey)
			throws PKIRAClientException;

	String signRequestContract(String requestContract, PrivateKeyEntry privateKeyEntry) throws PKIRAClientException;

	/**
	 * Sends the certificate signing request to the PKI RA web service. The URL
	 * of the web service has to be set beforehand using setServiceUrl().
	 * 
	 * @param signedRequestContract
	 *            the request contract containing the signing request. This
	 *            contract has to be signed.
	 * @return the response of the web service (already unmarshalled).
	 * @throws PKIRAClientException
	 *             if an error occurred contacting the web service or decoding
	 *             the result.
	 * @see #setServiceUrl
	 */
	CertificateSigningResponseType sendCertificateSigningRequest(String signedRequestContract)
			throws PKIRAClientException;

	/**
	 * Sends the certificate revocations request to the PKI RA web service. The
	 * URL of the web service has to be set beforehand using setServiceUrl().
	 * 
	 * @param signedRequestContract
	 *            the request contract containing the revocation request. This
	 *            contract has to be signed.
	 * @return the response of the web service (already unmarshalled).
	 * @throws PKIRAClientException
	 *             if an error occurred contacting the web service or decoding
	 *             the result.
	 * @see #setServiceUrl(String)
	 * @see #responseContainsErrors(ResponseType)
	 */
	CertificateRevocationResponseType sendCertificateRevocationRequest(String signedRequestContract)
			throws PKIRAClientException;

	/**
	 * Checks if the response from the web service contains an error.
	 * 
	 * @param response
	 *            response returned by sendCertificateSigningRequest() or
	 *            sendCertificateRevocationRequest().
	 */
	boolean responseContainsErrors(ResponseType response);

	/**
	 * Sets the URL of the eID PKI RA Public Webservice. This value can be
	 * obtained from Fedict.
	 * 
	 * @param serviceUrl
	 *            the new url.
	 */
	void setServiceUrl(String serviceUrl);

}
