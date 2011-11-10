package be.fedict.eid.pkira.blm.model.stats;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

public abstract class DateToLongReportGenerator extends KeyValueReportGenerator<Date, Long> {

	@Override
	protected final Long getZeroValue() {
		return 0L;
	}

	@Override
	protected final Iterator<Date> getIterator(final Date maxValue, final Date minValue) {
		if (maxValue == null || minValue == null) {
			return Collections.<Date> emptySet().iterator();
		}

		final Calendar startValue = new GregorianCalendar();
		startValue.setTime(maxValue);
		return new Iterator<Date>() {

			private final Calendar current = startValue;
			private Date nextValue = maxValue;

			@Override
			public boolean hasNext() {
				return !nextValue.before(minValue);
			}

			@Override
			public Date next() {
				Date result = nextValue;
				decrementIteratorDate(current);
				nextValue = current.getTime();

				return result;
			}

			@Override
			public void remove() {
			}
		};
	}

	protected abstract void decrementIteratorDate(Calendar current);

	@Override
	protected final Date getMaxValue(Collection<Date> values) {
		Date result = null;
		for (Date value : values) {
			if (result == null || value.after(result)) {
				result = value;
			}
		}
		return result;
	}

	@Override
	protected final Date getMinValue(Collection<Date> values) {
		Date result = null;
		for (Date value : values) {
			if (result == null || value.before(result)) {
				result = value;
			}
		}
		return result;
	}

}
