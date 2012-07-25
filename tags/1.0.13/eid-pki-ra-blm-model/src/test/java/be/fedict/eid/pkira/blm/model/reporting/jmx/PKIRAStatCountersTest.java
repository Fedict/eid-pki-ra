package be.fedict.eid.pkira.blm.model.reporting.jmx;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.model.DatabaseTest;

public class PKIRAStatCountersTest extends DatabaseTest {

	private PKIRAStatCounters counters;

	@BeforeMethod
	public void beforeMethod() {
		MockitoAnnotations.initMocks(this);

		counters = spy(new PKIRAStatCounters());
		doReturn(getEntityManager()).when(counters).getEntityManager();
	}

	@Test
	public void getCertificateDomains() {
		List<String> certificateDomains = counters.getCertificateDomains();
		assertEquals(certificateDomains, Arrays.asList("eHealth Client Certificates", "eHealth Server Certificates",
				"eHealth Code Signing Certificates", "Test", "eHealth Persons Certificates"));
	}

	@Test
	public void getFailedRequestCount() {
		assertEquals(counters.getFailedRequestCount("Test"), 1L);
	}

	@Test
	public void getSuccesfulRequestCount() {
		assertEquals(counters.getSuccessfulRequestCount("Test"), 2L);
	}
}
