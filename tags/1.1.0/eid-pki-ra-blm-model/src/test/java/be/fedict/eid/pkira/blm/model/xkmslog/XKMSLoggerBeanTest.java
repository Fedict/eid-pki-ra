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

package be.fedict.eid.pkira.blm.model.xkmslog;

import javax.persistence.EntityManager;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.xkmsws.XKMSLogger.XKMSMessageType;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.testng.Assert.*;

public class XKMSLoggerBeanTest {

	private class XKMSLogEntryAnswer implements Answer<XKMSLogEntry> {

		@Override
		public XKMSLogEntry answer(InvocationOnMock invocation) throws Throwable {
			captureLogEntry = (XKMSLogEntry) invocation.getArguments()[0];
			return captureLogEntry;
		}

	}

	@Mock
	private EntityManager entityManager;

	private XKMSLoggerBean loggerBean;

	private XKMSLogEntry captureLogEntry;

	@BeforeMethod
	public void beforeMethod() {
		MockitoAnnotations.initMocks(this);
		loggerBean = new XKMSLoggerBean();
		loggerBean.setEntityManager(entityManager);
		
		doAnswer(new XKMSLogEntryAnswer()).when(entityManager).persist(isA(XKMSLogEntry.class));
	}

	@Test
	public void logErrorDuringMarshalling() {
		loggerBean.logError(XKMSMessageType.REQUEST, null, null, new Exception("error"));

		assertNotNull(captureLogEntry);
		assertEquals(captureLogEntry.getMessageType(), XKMSMessageType.REQUEST);
		assertEquals(captureLogEntry.getErrorMessage(), "error");
		assertNull(captureLogEntry.getRequestMessage());
		assertNull(captureLogEntry.getResponseMessage());
		assertEquals(captureLogEntry.getStatus(), XKMSStatus.MARSHAL_ERROR);
		assertNotNull(captureLogEntry.getCreationDate());
	}
	
	@Test
	public void logErrorDuringExecution() {
		loggerBean.logError(XKMSMessageType.REQUEST, "request", null, new Exception("error"));

		assertNotNull(captureLogEntry);
		assertEquals(captureLogEntry.getMessageType(), XKMSMessageType.REQUEST);
		assertEquals(captureLogEntry.getErrorMessage(), "error");
		assertEquals("request", captureLogEntry.getRequestMessage());
		assertNull(captureLogEntry.getResponseMessage());
		assertEquals(captureLogEntry.getStatus(), XKMSStatus.XKMS_SERVICE_ERROR);
		assertNotNull(captureLogEntry.getCreationDate());
	}
	
	@Test
	public void logErrorUnmarshalling() {
		loggerBean.logError(XKMSMessageType.REQUEST, "request", "response".getBytes(), new Exception("error"));

		assertNotNull(captureLogEntry);
		assertEquals(captureLogEntry.getMessageType(), XKMSMessageType.REQUEST);
		assertEquals(captureLogEntry.getErrorMessage(), "error");
		assertEquals("request", captureLogEntry.getRequestMessage());
		assertEquals("response", captureLogEntry.getResponseMessage());
		assertEquals(captureLogEntry.getStatus(), XKMSStatus.RESULT_ERROR);
		assertNotNull(captureLogEntry.getCreationDate());
	}

	@Test
	public void logSuccesfulInteraction() {
		loggerBean.logSuccesfulInteraction(XKMSMessageType.REQUEST, "request", "response".getBytes());

		assertNotNull(captureLogEntry);
		assertEquals(captureLogEntry.getMessageType(), XKMSMessageType.REQUEST);
		assertNull(captureLogEntry.getErrorMessage());
		assertEquals("request", captureLogEntry.getRequestMessage());
		assertEquals("response", captureLogEntry.getResponseMessage());
		assertEquals(captureLogEntry.getStatus(), XKMSStatus.SUCCESS);
		assertNotNull(captureLogEntry.getCreationDate());
	}
}
