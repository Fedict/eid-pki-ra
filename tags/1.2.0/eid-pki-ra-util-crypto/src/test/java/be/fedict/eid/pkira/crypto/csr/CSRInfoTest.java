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

package be.fedict.eid.pkira.crypto.csr;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.crypto.exception.CryptoException;

import static org.testng.Assert.assertEquals;

public class CSRInfoTest {

    @Test
    public void canDetermineKeyLength() throws Exception {
        assertEquals(1024, readCSRFromResource("/test-keylength-1024.csr").getKeyLength());
        assertEquals(2048, readCSRFromResource("/test.csr").getKeyLength());
    }

    private CSRInfo readCSRFromResource(String resource) throws IOException, CryptoException {
        InputStream input = getClass().getResourceAsStream(resource);
        String csr = IOUtils.toString(input);
        return new CSRParserImpl().parseCSR(csr);
    }
}