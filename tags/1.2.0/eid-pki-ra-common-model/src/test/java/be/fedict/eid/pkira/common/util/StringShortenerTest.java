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

package be.fedict.eid.pkira.common.util;

import org.testng.Assert;
import org.testng.annotations.Test;

public class StringShortenerTest {

    public static final int MAX_LENGTH = 120;
    public static final String STR26 = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz";
    public static final String STR120 = STR26 + STR26 + STR26 + STR26 + "abcdefghijklmnop";
    public static final String STR130 = STR26 + STR26 + STR26 + STR26 + STR26;

    @Test
    public void shortenDistinguishedDoesNotShortenShortNames() throws Exception {
        Assert.assertEquals(STR26, StringShortener.shorten(STR26, MAX_LENGTH));
    }

    @Test
    public void shortenDistinguishedNameCanHandleNull() throws Exception {
        Assert.assertEquals(null, StringShortener.shorten(null, MAX_LENGTH));
    }

    @Test
    public void shortenDistinguishedShortensLongNames() throws Exception {
        Assert.assertEquals(STR120.substring(0, MAX_LENGTH) + "...", StringShortener.shorten(STR130, MAX_LENGTH));
    }

}