package be.fedict.eid.pkira.blm.model.ca;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.jboss.seam.annotations.Name;

@Entity
@Table(name="CA_PARAMETERS")
@Name(CertificateAuthorityParameter.NAME)
public class CertificateAuthorityParameter implements Serializable{
	
	private static final long serialVersionUID = 2378990512594315412L;
	
	public static final String NAME = "be.fedict.eid.pkira.blm.certificateAuthorityParameter";
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="CAP_ID")
	private Integer id;
	@JoinColumn(name="CA_CA_ID", nullable=false)
	@ManyToOne
	private CertificateAuthority ca;
	@Column(name="mapkey", nullable=false)
	private String key;
	@Column(name="element", nullable=false)
	private String value;
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}

	public void setCa(CertificateAuthority ca) {
		this.ca = ca;
	}

	public CertificateAuthority getCa() {
		return ca;
	}
}
