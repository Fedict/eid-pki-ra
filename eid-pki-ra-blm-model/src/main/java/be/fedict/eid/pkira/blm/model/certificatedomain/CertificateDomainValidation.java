package be.fedict.eid.pkira.blm.model.certificatedomain;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hibernate.validator.ValidatorClass;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ValidatorClass(CertificateDomainValidator.class)
public @interface CertificateDomainValidation {
	String message() default "{validator.invalid.certificateDomain}";
}
