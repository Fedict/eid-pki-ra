/*
 * eID PKI RA Project.
 * Copyright (C) 2010-2014 FedICT.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version
 * 3.0 as published by the Free Software Foundation.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, see
 * http://www.gnu.org/licenses/.
 */

package be.fedict.eid.pkira.portal.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.pkira.generated.privatews.CertificateTypeWS;
import be.fedict.eid.pkira.generated.privatews.CertificateWS;
import be.fedict.eid.pkira.generated.privatews.ContractTypeWS;
import be.fedict.eid.pkira.generated.privatews.ContractWS;
import be.fedict.eid.pkira.portal.certificate.Certificate;
import be.fedict.eid.pkira.portal.certificate.CertificateType;
import be.fedict.eid.pkira.portal.contract.Contract;
import be.fedict.eid.pkira.portal.contract.ContractType;

@Name(TypeMapper.NAME)
@Scope(ScopeType.STATELESS)
public class TypeMapper {
    public static final String NAME = "be.fedict.eid.pkira.portal.TypeMapper";

    private static DatatypeFactory datatypeFactory;

    public Contract map(ContractWS contractWS) {
        Contract contract = new Contract();
        contract.setId(contractWS.getContractId());
        contract.setCertificateDomainName(contractWS.getCertificateDomainName());
        if (contractWS.getCertificateType() != null) {
            contract.setCertificateType(Enum.valueOf(CertificateType.class, contractWS.getCertificateType().name()));
        }
        contract.setContractType(map(contractWS.getContractType()));
        contract.setCreationDate(contractWS.getCreationDate().toGregorianCalendar().getTime());
        contract.setDnExpression(contractWS.getDnExpression());
        contract.setCertificateId(contractWS.getCertificateId());
        contract.setRequesterName(contractWS.getRequesterName());
        contract.setResult(contractWS.getResult());
        contract.setResultMessage(contractWS.getResultMessage());
        return contract;
    }

    public ContractType map(ContractTypeWS contractTypeWS) {
        if (contractTypeWS==null) return null;
        return Enum.valueOf(ContractType.class, contractTypeWS.name());
    }

    public ContractTypeWS map(ContractType contractType) {
        if (contractType==null) return null;
        return Enum.valueOf(ContractTypeWS.class, contractType.name());
    }

    public List<Contract> mapContracts(List<ContractWS> contracts) {
        List<Contract> result = new ArrayList<Contract>();
        for (ContractWS contract : contracts) {
            result.add(map(contract));
        }
        return result;
    }

    public Certificate map(CertificateWS certificateWS) {
        Certificate certificate = new Certificate();
        certificate.setId(certificateWS.getCertificateId());
        certificate.setDistinguishedName(certificateWS.getDistinguishedName());
        certificate.setIssuer(certificateWS.getIssuer());
        certificate.setSerialNumber(certificateWS.getSerialNumber());
        certificate.setType(map(certificateWS.getCertificateType()));
        certificate.setValidityStart(map(certificateWS.getValidityStart()));
        certificate.setValidityEnd(map(certificateWS.getValidityEnd()));
        certificate.setZippedCertificates(certificateWS.getCertificateZip());
        certificate.setRequesterName(certificateWS.getRequesterName());
        certificate.setX509(certificateWS.getX509());
        return certificate;
    }

    public List<Certificate> mapCertificates(List<CertificateWS> certificates) {
        List<Certificate> result = new ArrayList<Certificate>();
        for (CertificateWS certificate : certificates) {
            result.add(map(certificate));
        }
        return result;
    }

    public CertificateType map(CertificateTypeWS certificateTypeWS) {
        if (certificateTypeWS==null) return null;
        return Enum.valueOf(CertificateType.class, certificateTypeWS.name());
    }

    public CertificateTypeWS map(CertificateType certificateType) {
        if (certificateType==null) return null;
        return Enum.valueOf(CertificateTypeWS.class, certificateType.name());
    }

    public <T extends Enum<T>> T getEnum(Class<T> enumClass, String name) {
        if (StringUtils.isBlank(name)) return null;
        return Enum.valueOf(enumClass, name);
    }

    public Date map(XMLGregorianCalendar xmlGregorianCalendar) {
        if (xmlGregorianCalendar == null) {
            return null;
        } else {
            return xmlGregorianCalendar.toGregorianCalendar().getTime();
        }
    }

    public XMLGregorianCalendar map(Date date) {
        if (date==null) return null;

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));

        return getDatatypeFactory().newXMLGregorianCalendar(calendar);
    }

    public synchronized static DatatypeFactory getDatatypeFactory() {
        if (datatypeFactory == null) {
            try {
                datatypeFactory = DatatypeFactory.newInstance();
            } catch (DatatypeConfigurationException e) {
                throw new RuntimeException("Cannot get DatatypeFactory", e);
            }
        }
        return datatypeFactory;
    }
}
