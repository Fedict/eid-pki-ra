package be.fedict.eid.pkira.blm.model.stats;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Interface to be implemented by statistical reports.
 * 
 * @author jan
 */
public interface StatisticsReportGenerator {

	/**
	 * Column type.
	 */
	enum ReportColumnType {
		DATE, DATETIME, LONG, FLOAT, LABEL
	}

	/**
	 * Column in a report.
	 */
	class ReportColumn {

		private final ReportColumnType type;
		private final String name;
		private final String format;

		public ReportColumn(ReportColumnType type, String name) {
			this.type = type;
			this.name = name;
			this.format = null;
		}

		public ReportColumn(ReportColumnType type, String name, String format) {
			this.type = type;
			this.name = name;
			this.format = format;
		}

		/**
		 * Reference in messages.properties with the name of the column.
		 */
		public String getName() {
			return name;
		}

		/**
		 * Returns the type of the column.
		 */
		public ReportColumnType getType() {
			return type;
		}

		@Override
		public String toString() {
			return name + " (" + type.name() + ")";
		}

		public String getFormat() {
			return format;
		}
	}

	/**
	 * Value in the report.
	 */
	class ReportValue {

		private final Object value;
		private final ReportColumn column;

		public ReportValue(ReportColumn column, Object value) {
			this.column = column;
			this.value = value;
		}

		public Object getValue() {
			return value;
		}

		public ReportColumn getColumn() {
			return column;
		}
	}

	/**
	 * Row in the report.
	 */
	class ReportRow {

		private final List<ReportValue> values;

		public ReportRow(List<ReportValue> values) {
			this.values = Collections.unmodifiableList(values);
		}

		public ReportRow(ReportValue... values) {
			this(Arrays.asList(values));
		}

		public List<ReportValue> getValues() {
			return values;
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return super.toString();
		}

	}

	/**
	 * Reference of the bean. Is also used to determine the display name in
	 * messages.properties.
	 */
	String getName();

	/**
	 * Returns the list of columns in the report.
	 */
	List<ReportColumn> getReportColumns();

	/**
	 * Returns the actual report data.
	 */
	List<ReportRow> getReport();
}
