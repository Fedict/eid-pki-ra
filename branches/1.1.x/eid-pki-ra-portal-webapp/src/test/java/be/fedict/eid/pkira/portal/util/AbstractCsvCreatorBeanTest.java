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

import java.util.Date;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.common.download.Document;
import be.fedict.eid.pkira.common.download.DownloadManager;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class AbstractCsvCreatorBeanTest {

    public static class TestCsvCreator extends AbstractCsvCreatorBean {
        public void createCvs() {
            CsvBuilder builder = new CsvBuilder();
            builder.append("abc").append(new Date(1398172081265L)).nextRow().download("fileName", "contentType");
        }
    }

    @InjectMocks private TestCsvCreator csvCreator = new TestCsvCreator();

    @Mock private DownloadManager downloadManager;
    @Mock private ConfigurationEntryContainer configurationEntryContainer;

    @BeforeMethod
    public void setup() {
        initMocks(this);
        doReturn(";").when(configurationEntryContainer).getCsvExportFieldSeparator();
    }

    @Test
    public void canGenerateCsv() {
        csvCreator.createCvs();

        ArgumentCaptor<Document> documentArgumentCaptor = ArgumentCaptor.forClass(Document.class);
        verify(downloadManager).download(documentArgumentCaptor.capture());

        Document document = documentArgumentCaptor.getValue();
        assertNotNull(document);
        assertEquals(document.getFileName(), "fileName");
        assertEquals(document.getContentType(), "contentType");
        assertEquals(new String(document.getData()), "abc;22/04/2014;\r\n");
    }
}