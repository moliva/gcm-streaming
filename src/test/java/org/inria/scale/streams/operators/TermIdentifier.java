package org.inria.scale.streams.operators;

import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.not;
import static org.inria.scale.streams.utils.Functions.trim;

import java.util.List;

import org.inria.scale.streams.base.BaseOperator;
import org.inria.scale.streams.configuration.TermIdentifierConfiguration;
import org.javatuples.Pair;
import org.javatuples.Tuple;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

public class TermIdentifier extends BaseOperator implements TermIdentifierConfiguration {

	private String termsString;

	@Override
	protected List<? extends Tuple> processTuples(final List<Tuple> tuplesToProcess) {
		final List<String> terms = createTerms();

		return FluentIterable.from(tuplesToProcess).transform(new Function<Tuple, Tuple>() {

			@Override
			public Tuple apply(final Tuple tuple) {
				final String text = (String) tuple.getValue(0);
				final List<String> identifiedTerms = identifyTerms(text, terms);
				return Pair.with(text, identifiedTerms);
			}
		}).toList();
	}

	private List<String> identifyTerms(final String text, final List<String> terms) {
		return FluentIterable.from(terms).filter(new Predicate<String>() {

			@Override
			public boolean apply(final String term) {
				return text.contains(term);
			}
		}).toList();
	}

	private List<String> createTerms() {
		return FluentIterable.of(termsString.split(",")) //
				.transform(trim()) //
				.filter(not(equalTo(""))) //
				.toList();
	}

	// //////////////////////////////////////////////
	// ******* PeopleIdentifierConfiguration *******
	// //////////////////////////////////////////////

	@Override
	public String getTerms() {
		return termsString;
	}

	@Override
	public void setTerms(final String termsString) {
		this.termsString = termsString;
	}

}
