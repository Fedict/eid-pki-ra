package be.fedict.eid.pkira.blm.model.xkmslog;

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;

@Name(XKMSLogListHandler.NAME)
@Scope(ScopeType.CONVERSATION)
public class XKMSLogListHandlerBean implements XKMSLogListHandler {

	private static final long serialVersionUID = 170377907904773374L;

	private static final String LOGENTRIES_NAME = "xkmsLogEntries";

	@DataModel
	private List<XKMSLogEntry> xkmsLogEntries;

	@In(value = XKMSLogEntryHome.NAME, create = true)
	private XKMSLogEntryHome xkmsLogEntryHome;
	
	@In(value = XKMSLogEntryQuery.NAME, create = true)
	private XKMSLogEntryQuery xkmsLogEntryQuery;

	@Override
	public List<XKMSLogEntry> findLogList() {
		xkmsLogEntries = xkmsLogEntryQuery.getResultList();
		return xkmsLogEntries;
	}

	@Factory(LOGENTRIES_NAME)
	public void initCertificateList() {
		findLogList();
	}

	public List<XKMSLogEntry> getXkmsLogEntries() {
		return xkmsLogEntries;
	}

	@Override
	public void downloadRequest(Integer id) {
		xkmsLogEntryHome.setId(id);
		xkmsLogEntryHome.downloadRequest();
	}

	@Override
	public void downloadResponse(Integer id) {
		xkmsLogEntryHome.setId(id);
		xkmsLogEntryHome.downloadResponse();
	}
}
