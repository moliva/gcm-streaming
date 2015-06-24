package org.inria.scale.streams.operators;

import static java.lang.Integer.parseInt;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.inria.scale.streams.base.BaseOperator;
import org.inria.scale.streams.configuration.SentimentClassifierConfiguration;
import org.javatuples.Pair;
import org.javatuples.Tuple;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

/**
 * Based on a tuple and a component index analyzes the sentiment value as a sum
 * of the words contained and the AFINN dictionary.
 *
 * @author moliva
 *
 */
public class DictionarySentimentClassifier extends BaseOperator implements SentimentClassifierConfiguration {

	public static final String DEFAULT_CHARSET = "UTF-8";
	public static final int DEFAULT_INDEX = 0;
	public static final String DEFAULT_PATH = "dictionaries/AFINN/AFINN-111.txt";

	private int componentIndex = DEFAULT_INDEX;
	private String pathInResources = DEFAULT_PATH;
	private String charset;

	@Override
	public List<? extends Tuple> processTuples(final List<Tuple> tuplesToProcess) {
		try {
			final Map<String, Integer> dictionary = readDictionary();

			final List<Tuple> resultingTuples = FluentIterable.from(tuplesToProcess).transform(new Function<Tuple, Tuple>() {

				@Override
				public Tuple apply(final Tuple tuple) {
					final Object value = tuple.getValue(componentIndex);
					if (value instanceof String) {
						final String text = (String) value;
						final int sum = valueSumFor(dictionary, text);
						return Pair.with(text, sum);
					}

					return tuple;
				}

			}).toList();

			return resultingTuples;

		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	private int valueSumFor(final Map<String, Integer> dictionary, final String text) {
		final String[] words = text.split("\\s+");
		int sum = 0;
		for (final String word : words) {
			final Integer v = dictionary.get(word);
			if (v != null) {
				sum += v;
			}
		}
		return sum;
	}

	private Map<String, Integer> readDictionary() throws IOException {
		final InputStream stream = getClass().getClassLoader().getResourceAsStream(pathInResources);
		final String tsvDictionary = IOUtils.toString(stream, charset);

		final String[] lines = tsvDictionary.split(System.getProperty("line.separator"));

		final HashMap<String, Integer> values = new HashMap<>();
		for (final String line : lines) {
			try {
				final String[] wordValue = line.split("\t");
				if (wordValue.length == 2) {
					values.put(wordValue[0], parseInt(wordValue[1]));
				}
			} catch (final NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return values;
	}

	// //////////////////////////////////////////////
	// ******* SentimentClassifierConfiguration ***
	// //////////////////////////////////////////////

	@Override
	public int getComponentIndex() {
		return componentIndex;
	}

	@Override
	public void setComponentIndex(final int componentIndex) {
		this.componentIndex = componentIndex;
	}

	@Override
	public String getPathInResources() {
		return pathInResources;
	}

	@Override
	public void setPathInResources(final String pathInResources) {
		this.pathInResources = pathInResources;
	}

	@Override
	public String getCharset() {
		return charset;
	}

	@Override
	public void setCharset(final String charset) {
		this.charset = charset;
	}

}
