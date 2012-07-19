package be.fedict.eid.pkira.blm.model.reporting.jmx;

import java.util.List;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.StandardMBean;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.log.Log;

@Name("pkiraStatCounterRegistration")
@Scope(ScopeType.APPLICATION)
@Startup
@AutoCreate
public class PKIRAStatCountersRegistration {

	private static final String JMX_OBJECT_NAME = "be.fedict.pkira:name=CertificateRequestStatistics";
	private static final String JMX_SERVER_DEFAULT_DOMAIN = "jboss";

	@Logger
	private Log log;

	@In(value=PKIRAStatCountersMBean.NAME, create=true)
	private PKIRAStatCountersMBean statCounters;

	private MBeanServer server;
	private ObjectName objectName;

	@Create
	public void registerMBean() {
		log.info("Registering PKIRAStatCounters bean with server.");

		// Find the MBean server
		server = findMBeanServer();
		if (server == null) {
			log.error("No MBeanServer found. JMX bean was not registered.");
			return;
		}

		// Register the bean with the MBean server
		try {
			objectName = new ObjectName(JMX_OBJECT_NAME);
			StandardMBean mbean = new StandardMBean(statCounters, PKIRAStatCountersMBean.class);

			server.registerMBean(mbean, objectName);
		} catch (JMException e) {
			log.error("Could not register mbean. JMX bean was not registered.", e);
		}

		log.info("Registered PKIRAStatCounters bean with server.");
	}

	@Destroy
	public void unregisterMBean() {
		if (server != null && objectName != null) {
			log.info("Unregistering PKIRAStatCounters bean with server.");
			try {
				server.unregisterMBean(objectName);
			} catch (JMException e) {
				log.error("Could not unregister mbean.", e);
			}

			log.info("Unregistered PKIRAStatCounters bean with server.");
		}

		statCounters = null;
		server = null;
		objectName = null;
	}

	private MBeanServer findMBeanServer() {
		List<MBeanServer> servers = MBeanServerFactory.findMBeanServer(null);
		for (MBeanServer server : servers) {
			if (server.getDefaultDomain().equals(JMX_SERVER_DEFAULT_DOMAIN)) {
				return server;
			}
		}

		return null;
	}
}
