package com.rolfje.anonimatron.anonymizer;

import com.rolfje.anonimatron.synonyms.DateSynonym;
import com.rolfje.anonimatron.synonyms.Synonym;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

class DateAnonymizer implements Anonymizer {
	private static final String TYPE = "DATE";
	private static final long RANDOMIZATION_MILLIS = 1000L * 60L * 60L * 24L * 31L;

	private Set<Date> generatedDates = new HashSet<>();

	@Override
	public Synonym anonymize(Object from, int size, boolean shortlived) {
		DateSynonym s = new DateSynonym();
		s.setType(TYPE);
		s.setShortlived(shortlived);

		if (from == null) {
			s.setFrom(null);
			s.setTo(null);
		} else if (from instanceof Date) {
			s.setFrom(from);

			long originalepoch = ((Date) from).getTime();

			Date newDate;
			do {
				long deviation = Math.round(2 * RANDOMIZATION_MILLIS
						* Math.random())
						- RANDOMIZATION_MILLIS;
				newDate = new Date(originalepoch + deviation);
			} while (generatedDates.contains(newDate));

			generatedDates.add(newDate);
			s.setTo(newDate);
		} else {
			throw new UnsupportedOperationException(
					"Can not anonymize objects of type " + from.getClass());
		}

		return s;
	}


	@Override
	public String getType() {
		return TYPE;
	}
}
