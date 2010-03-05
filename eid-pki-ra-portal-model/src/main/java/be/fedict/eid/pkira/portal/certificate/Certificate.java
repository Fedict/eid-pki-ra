package be.fedict.eid.pkira.portal.certificate;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.pkira.generated.privatews.Certificatews;

@Entity
@Name("certificate")
@Scope(ScopeType.EVENT)
public class Certificate {
	
	@Id
	private int id;
	private BigInteger serialNumber;
	private String subject;
	private Date validityStart;
	private Date validityEnd;
	private String requesterName;
	private String issuer;

	public Certificate(){
	}
	
	public Certificate(Certificatews certificatews){
		BigInteger bigInteger = new BigInteger(certificatews.getSerialNumber());
		setSerialNumber(bigInteger);
		setSubject(certificatews.getSubject());
		setValidityStart(certificatews.getValidityStart().toGregorianCalendar().getTime());
		setValidityEnd(certificatews.getValidityEnd().toGregorianCalendar().getTime());
		setRequesterName(certificatews.getRequesterName());
		setIssuer(certificatews.getIssuer());
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the validityStart
	 */
	public Date getValidityStart() {
		return validityStart;
	}

	/**
	 * @param validityStart the validityStart to set
	 */
	public void setValidityStart(Date validityStart) {
		this.validityStart = validityStart;
	}

	/**
	 * @return the validityEnd
	 */
	public Date getValidityEnd() {
		return validityEnd;
	}

	/**
	 * @param validityEnd the validityEnd to set
	 */
	public void setValidityEnd(Date validityEnd) {
		this.validityEnd = validityEnd;
	}

	/**
	 * @return the requesterName
	 */
	public String getRequesterName() {
		return requesterName;
	}

	/**
	 * @param requesterName the requesterName to set
	 */
	public void setRequesterName(String requesterName) {
		this.requesterName = requesterName;
	}

	/**
	 * @return the issuer
	 */
	public String getIssuer() {
		return issuer;
	}

	/**
	 * @param issuer the issuer to set
	 */
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	/**
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(BigInteger serialNumber) {
		this.serialNumber = serialNumber;
	}

	/**
	 * @return the serialNumber
	 */
	public BigInteger getSerialNumber() {
		return serialNumber;
	}
}
