package org.inria.scale.streams.aggregators;

import java.util.List;
import java.util.Set;

import org.inria.scale.streams.base.BaseTwoSourcesAggregator;
import org.inria.scale.streams.configuration.CoGroupConfiguration;
import org.javatuples.Triplet;
import org.javatuples.Tuple;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;

/**
 * Joins two sets of tuples by combining them according to the specified keys
 * for each one. The resulting tuple from this operation will be {@link Triplet
 * triplets} with the form (key, values in first set w.r.t. key, values in
 * second set w.r.t. key).
 * 
 * @author moliva
 *
 */
public class CoGroup extends BaseTwoSourcesAggregator implements CoGroupConfiguration {

	private int indexKey0;
	private int indexKey1;

	// //////////////////////////////////////////////
	// ******* BaseTwoSourcesAggregator *******
	// //////////////////////////////////////////////

	@Override
	protected List<? extends Tuple> processTuples(final List<Tuple> tuples0, final List<Tuple> tuples1) {
		final Set<Object> keys = collectKeys(tuples0, tuples1);

		return FluentIterable.from(keys).transform(makeTripletWithGroups(tuples0, tuples1)).toList();
	}

	private Function<Object, Tuple> makeTripletWithGroups(final List<Tuple> tuples0, final List<Tuple> tuples1) {
		return new Function<Object, Tuple>() {

			@Override
			public Tuple apply(final Object key) {
				return Triplet.with(key, collectValues(key, indexKey0, tuples0), collectValues(key, indexKey1, tuples1));
			}
		};
	}

	protected List<Tuple> collectValues(final Object key, final int index, final List<Tuple> tuples) {
		return FluentIterable.from(tuples).filter(new Predicate<Tuple>() {

			@Override
			public boolean apply(final Tuple input) {
				return key.equals(input.getValue(index));
			}
		}).toList();
	}

	private Set<Object> collectKeys(final List<Tuple> tuples0, final List<Tuple> tuples1) {
		final Set<Object> keys0 = collectKeys(indexKey0, tuples0);
		final Set<Object> keys1 = collectKeys(indexKey1, tuples1);

		return Sets.union(keys0, keys1);
	}

	private Set<Object> collectKeys(final int key, final List<Tuple> tuples) {
		return FluentIterable.from(tuples).transform(new Function<Tuple, Object>() {

			@Override
			public Object apply(final Tuple input) {
				return input.getValue(key);
			}

		}).toSet();
	}

	// //////////////////////////////////////////////
	// ******* CoGroupConfiguration *******
	// //////////////////////////////////////////////

	@Override
	public int getIndexKey0() {
		return indexKey0;
	}

	@Override
	public void setIndexKey0(final int indexKey0) {
		this.indexKey0 = indexKey0;
	}

	@Override
	public int getIndexKey1() {
		return indexKey1;
	}

	@Override
	public void setIndexKey1(final int indexKey1) {
		this.indexKey1 = indexKey1;
	}

}
