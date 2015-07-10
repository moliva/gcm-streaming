package org.inria.scale.streams.tests.unit.operators;

import static org.hamcrest.Matchers.contains;
import static org.inria.scale.streams.tests.utils.TupleUtils.tupleWith;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.inria.scale.streams.operators.DictionarySentimentClassifier;
import org.javatuples.Tuple;
import org.junit.Before;
import org.junit.Test;

public class DictionarySentimentClassifierTest {

	private final DictionarySentimentClassifier classifier = new DictionarySentimentClassifier();

	@Before
	public void initializeClassifier() {
		classifier.setComponentIndex(0);
		classifier.setPathInResources("dictionaries/test-dictionary.txt");
	}

	@Test
	public void shouldMapSingleWordValuesFromDictionary() throws Exception {
		final Tuple[] tuplesToProcess = new Tuple[] { tupleWith("wow"), tupleWith("abhors") };

		final List<? extends Tuple> tuples = classifier.processTuples(Arrays.asList(tuplesToProcess));

		assertThat(tuples, contains(tupleWith("wow", 4.0), tupleWith("abhors", -3.0)));
	}

	@Test
	public void shouldMapMultiplesWordValuesFromDictionary() throws Exception {
		final Tuple[] tuplesToProcess = new Tuple[] { tupleWith("amazing abductions wow"), tupleWith("aboard abhors") };

		final List<? extends Tuple> tuples = classifier.processTuples(Arrays.asList(tuplesToProcess));

		assertThat(tuples, contains(tupleWith("amazing abductions wow", 2.0), tupleWith("aboard abhors", -1.0)));
	}

	@Test
	public void shouldSetTextOutsideOfDictionaryToZero() throws Exception {
		final Tuple[] tuplesToProcess = new Tuple[] { tupleWith("hunted neighbor"), tupleWith("random yesterday snore") };

		final List<? extends Tuple> tuples = classifier.processTuples(Arrays.asList(tuplesToProcess));

		assertThat(tuples, contains(tupleWith("hunted neighbor", 0.0), tupleWith("random yesterday snore", -1.0)));
	}
}
