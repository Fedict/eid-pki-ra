package be.fedict.eid.pkira.blm.model.stats;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Context;
import org.jboss.seam.contexts.Contexts;

import be.fedict.eid.pkira.blm.model.stats.StatisticsReportGenerator.ReportColumn;
import be.fedict.eid.pkira.blm.model.stats.StatisticsReportGenerator.ReportRow;
import be.fedict.eid.pkira.blm.model.stats.StatisticsReportGenerator.ReportValue;
import be.fedict.eid.pkira.common.download.Document;
import be.fedict.eid.pkira.common.download.DownloadManager;

/**
 * Bean used by the page to select statistics.
 * 
 * @author jan
 */
@Name(StatisticsBean.NAME)
@Scope(ScopeType.SESSION)
public class StatisticsBean {

	private static final String LINE_SEPARATOR = "\r\n";
	private static final String FIELD_QUOTE_SYMBOL = "\"";
	private static final String FIELD_SEPARATOR = ";";

	public static final String NAME = "be.fedict.eid.pkira.blm.statistics";

	@In(value = DownloadManager.NAME, create = true)
	private DownloadManager downloadManager;
	
	@In 
	private Map<String, String> messages;

	private List<StatisticsReportGenerator> reportGenerators;
	private String reportGeneratorName;
	private StatisticsReportGenerator reportGenerator;

	/**
	 * Retrieves a list of all the defined report generators in Seam.
	 * 
	 * @return
	 */
	public synchronized List<StatisticsReportGenerator> getReportGenerators() {
		if (reportGenerators == null) {
			reportGenerators = new ArrayList<StatisticsReportGenerator>();
			Context context = Contexts.getApplicationContext();
			for (String name : context.getNames()) {
				Object object = context.get(name);
				if (object instanceof org.jboss.seam.Component) {
					Component component = (Component) object;
					if (component.getBusinessInterfaces().contains(StatisticsReportGenerator.class)) {
						reportGenerators.add((StatisticsReportGenerator) Component.getInstance(component.getName()));
					}
				}
			}

			Collections.sort(reportGenerators, new Comparator<StatisticsReportGenerator>() {

				@Override
				public int compare(StatisticsReportGenerator o1, StatisticsReportGenerator o2) {
					return o1.getName().compareToIgnoreCase(o2.getName());
				}
			});

			reportGenerators = Collections.unmodifiableList(reportGenerators);
		}

		return reportGenerators;
	}

	public void setReportGenerators(List<StatisticsReportGenerator> reportGenerators) {
		this.reportGenerators = reportGenerators;
	}

	public String getReportGeneratorName() {
		return reportGeneratorName;
	}

	public void setReportGeneratorName(String reportGeneratorName) {
		this.reportGeneratorName = reportGeneratorName;
	}

	public void selectReportType() {
		if (reportGeneratorName != null) {
			this.reportGenerator = (StatisticsReportGenerator) Component.getInstance(reportGeneratorName, true);
		} else {
			this.reportGenerator = null;
		}
	}

	public void csvExport() {
		if (reportGenerator == null) {
			return;
		}

		StringBuilder csv = new StringBuilder();

		// add column headers
		boolean first = true;
		for (ReportColumn column : reportGenerator.getReportColumns()) {
			if (!first) {
				csv.append(FIELD_SEPARATOR);
			}
			csv.append(FIELD_QUOTE_SYMBOL);
			if (messages.containsKey(column.getName())) {
				csv.append(messages.get(column.getName()));
			} else {
				csv.append(column.getName());
			}
			csv.append(FIELD_QUOTE_SYMBOL);
			first = false;
		}
		csv.append(LINE_SEPARATOR);

		// add rows
		for (ReportRow row : reportGenerator.getReport()) {
			first = true;
			for (ReportValue value : row.getValues()) {
				if (!first) {
					csv.append(FIELD_SEPARATOR);
				}

				csv.append(FIELD_QUOTE_SYMBOL);
				if (!StringUtils.isEmpty(value.getColumn().getFormat())) {
					DateFormat format = new SimpleDateFormat(value.getColumn().getFormat());
					csv.append(format.format(value.getValue()));
				} else {
					csv.append(value.getValue());
				}
				csv.append(FIELD_QUOTE_SYMBOL);
				
				first = false;
			}
			csv.append(LINE_SEPARATOR);
		}
		downloadManager.download(new Document("pkira-statistics.csv", "text/csv", csv.toString().getBytes()));
	}

	public StatisticsReportGenerator getReportGenerator() {
		return reportGenerator;
	}

	public void setDownloadManager(DownloadManager downloadManager) {
		this.downloadManager = downloadManager;
	}

}
