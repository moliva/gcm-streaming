package org.inria.scale.streams.tests.unit.operators;

import static org.hamcrest.Matchers.contains;
import static org.inria.scale.streams.tests.utils.TupleUtils.tupleWith;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.inria.scale.streams.operators.TextNormalizer;
import org.javatuples.Tuple;
import org.junit.Test;

public class TextNormalizerTest {

	private final TextNormalizer operator = new TextNormalizer();

	@Test
	public void shouldNormalizeStringsInTextTuples() throws Exception {
		final List<Tuple> tuples = Arrays.asList(tupleWith("UnOrmalized stRing!"), tupleWith("  HellO, world?"));

		final List<? extends Tuple> processedTuples = operator.processTuples(tuples);

		assertThat(processedTuples, contains(tupleWith("unormalized string"), tupleWith("hello world")));
	}

	@Test
	public void shouldForwardSameTuplesIfNotTextTuples() throws Exception {
		final List<Tuple> tuples = Arrays.asList(tupleWith(1), tupleWith('b'));

		final List<? extends Tuple> processedTuples = operator.processTuples(tuples);

		assertThat(processedTuples, contains(tupleWith(1), tupleWith('b')));
	}

}
