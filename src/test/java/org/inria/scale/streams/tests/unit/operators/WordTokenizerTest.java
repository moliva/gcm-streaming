package org.inria.scale.streams.tests.unit.operators;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.inria.scale.streams.tests.utils.TupleUtils.tupleWith;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.inria.scale.streams.operators.WordTokenizer;
import org.javatuples.Tuple;
import org.junit.Test;

public class WordTokenizerTest {

	private final WordTokenizer operator = new WordTokenizer();

	@Test
	public void shouldTokenizeWordsInTextTuples() throws Exception {
		final List<Tuple> tuples = Arrays.asList( //
				tupleWith("Have you got colour in your cheeks?"), //
				tupleWith("Do you ever get that fear that you can't shift"));

		final List<? extends Tuple> processedTuples = operator.processTuples(tuples);

		assertThat(
				processedTuples,
				contains(tupleWith("Have"), tupleWith("you"), tupleWith("got"), tupleWith("colour"), tupleWith("in"),
						tupleWith("your"), tupleWith("cheeks?"), tupleWith("Do"), tupleWith("you"), tupleWith("ever"),
						tupleWith("get"), tupleWith("that"), tupleWith("fear"), tupleWith("that"), tupleWith("you"),
						tupleWith("can't"), tupleWith("shift")));
	}

	@Test
	public void shouldReturnAnEmptyListIfTheTuplesHaveNotStringComponents() throws Exception {
		final List<Tuple> tuples = Arrays.asList( //
				tupleWith('a', 'b', 'c'), //
				tupleWith(1, 2l, 3.0f, 4.0d), //
				tupleWith(new Object()));

		final List<? extends Tuple> processedTuples = operator.processTuples(tuples);

		assertThat(processedTuples, is(empty()));
	}

	@Test
	public void shouldReturnTheTokensFromStringComponentsInMixedTuples() throws Exception {
		final List<Tuple> tuples = Arrays.asList(tupleWith('a', 2, "this is the third", new Object()));

		final List<? extends Tuple> processedTuples = operator.processTuples(tuples);

		assertThat(processedTuples, contains(tupleWith("this"), tupleWith("is"), tupleWith("the"), tupleWith("third")));
	}

}
