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
package be.fedict.eid.pkira.contracts.util;

import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.XMLGregorianCalendar;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class JAXBUtilTest {

	private static final int SECOND = 23;
	private static final int MINUTE = 30;
	private static final int HOUR = 10;
	private static final int DAY = 25;
	private static final int MONTH = 2;
	private static final int YEAR = 2010;

	@Test
	public void testGetMarshaller() {
		assertNotNull(JAXBUtil.getMarshaller());
	}

	@Test
	public void testGetUnmarshaller() {
		assertNotNull(JAXBUtil.getUnmarshaller());
	}

	@Test
	public void testGetObjectFactory() {
		assertNotNull(JAXBUtil.getObjectFactory());
	}

	@Test
	public void testGetDatatypeFactory() {
		assertNotNull(JAXBUtil.getDatatypeFactory());
	}
	
	@Test
	public void testCreateXmlGregorianCalendar() {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeZone(TimeZone.getTimeZone("CET"));
		calendar.set(YEAR, MONTH-1, DAY, HOUR, MINUTE, SECOND);
		
		XMLGregorianCalendar xmlCalendar = JAXBUtil.createXmlGregorianCalendar(calendar.getTime());
		assertNotNull(xmlCalendar);
		assertEquals(xmlCalendar.getYear(), YEAR);
		assertEquals(xmlCalendar.getMonth(), MONTH);
		assertEquals(xmlCalendar.getDay(), DAY);
		assertEquals(xmlCalendar.getHour(), HOUR-1);
		assertEquals(xmlCalendar.getMinute(), MINUTE);
		assertEquals(xmlCalendar.getSecond(), SECOND);
		
	}
}
