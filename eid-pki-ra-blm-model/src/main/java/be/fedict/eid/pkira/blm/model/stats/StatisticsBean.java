package be.fedict.eid.pkira.blm.model.stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Context;
import org.jboss.seam.contexts.Contexts;

/**
 * Bean used by the page to select statistics.
 * 
 * @author jan
 */
@Name(StatisticsBean.NAME)
@Scope(ScopeType.SESSION)
public class StatisticsBean {

	public static final String NAME = "be.fedict.eid.pkira.blm.statistics";

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

	public StatisticsReportGenerator getReportGenerator() {
		return reportGenerator;
	}

}
