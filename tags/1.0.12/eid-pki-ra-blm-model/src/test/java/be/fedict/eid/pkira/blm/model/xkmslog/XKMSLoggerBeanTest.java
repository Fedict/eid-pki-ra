package be.fedict.eid.pkira.blm.model.xkmslog;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import javax.persistence.EntityManager;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.xkmsws.XKMSLogger.XKMSMessageType;

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
