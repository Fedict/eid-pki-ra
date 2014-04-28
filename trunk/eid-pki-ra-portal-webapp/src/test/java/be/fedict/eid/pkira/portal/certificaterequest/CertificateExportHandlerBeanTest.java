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

package be.fedict.eid.pkira.portal.certificaterequest;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.common.download.Document;
import be.fedict.eid.pkira.common.download.DownloadManager;
import be.fedict.eid.pkira.common.security.EIdUser;
import be.fedict.eid.pkira.common.security.EIdUserCredentials;
import be.fedict.eid.pkira.generated.privatews.CertificateWS;
import be.fedict.eid.pkira.portal.certificate.Certificate;
import be.fedict.eid.pkira.portal.certificate.CertificateExportHandlerBean;
import be.fedict.eid.pkira.portal.certificate.CertificateType;
import be.fedict.eid.pkira.portal.util.ConfigurationEntryContainer;
import be.fedict.eid.pkira.portal.util.TypeMapper;
import be.fedict.eid.pkira.privatews.EIDPKIRAPrivateServiceClient;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class CertificateExportHandlerBeanTest {

    private static final String RRN = "RRN";

    @InjectMocks
    private CertificateExportHandlerBean certificateExportHandlerBean = new CertificateExportHandlerBean();

    @Mock private DownloadManager downloadManager;
    @Mock private ConfigurationEntryContainer configurationEntryContainer;
    @Mock private EIDPKIRAPrivateServiceClient privateServiceClient;
    @Mock private TypeMapper typeMapper;
    @Mock private EIdUserCredentials credentials;

    @Mock private Map<String, String> messages;

    @BeforeMethod
    public void setup() {
        initMocks(this);

        doReturn(";").when(configurationEntryContainer).getCsvExportFieldSeparator();
        doReturn(new EIdUser(RRN, null, null)).when(credentials).getUser();
    }

    @Test
    public void testExportToCSV() throws Exception {
        CertificateWS certificateWS = new CertificateWS();
        List<CertificateWS> certificateWSes = Collections.singletonList(certificateWS);
        doReturn(certificateWSes).when(privateServiceClient).findCertificates(RRN, null, null, null);

        Certificate certificate = new Certificate();
        certificate.setType(CertificateType.CLIENT);
        doReturn(Collections.singletonList(certificate)).when(typeMapper).mapCertificates(certificateWSes);

        certificateExportHandlerBean.exportToCSV();

        verify(downloadManager).download(any(Document.class));
    }
}