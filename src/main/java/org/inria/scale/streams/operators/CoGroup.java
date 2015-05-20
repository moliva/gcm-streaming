package org.inria.scale.streams.operators;

import java.util.List;
import java.util.Set;

import org.inria.scale.streams.base.TwoWayCombinator;
import org.inria.scale.streams.configuration.CoGroupConfiguration;
import org.javatuples.Triplet;
import org.javatuples.Tuple;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;

public class CoGroup extends TwoWayCombinator implements CoGroupConfiguration {

	private int key0;
	private int key1;

	// //////////////////////////////////////////////
	// ******* TwoWayCombinator *******
	// //////////////////////////////////////////////

	@Override
	protected List<? extends Tuple> process(final List<Tuple> tuples0, final List<Tuple> tuples1) {
		final Set<Object> keys = collectKeys(tuples0, tuples1);

		return FluentIterable.from(keys).transform(makeTripletWithGroups(tuples0, tuples1)).toList();
	}

	private Function<Object, Tuple> makeTripletWithGroups(final List<Tuple> tuples0, final List<Tuple> tuples1) {
		return new Function<Object, Tuple>() {

			@Override
			public Tuple apply(final Object key) {
				return Triplet.with(key, collectValues(key, key0, tuples0), collectValues(key, key1, tuples1));
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
		final Set<Object> keys0 = collectKeys(key0, tuples0);
		final Set<Object> keys1 = collectKeys(key1, tuples1);

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
	public int getKey0() {
		return key0;
	}

	@Override
	public void setKey0(final int key0) {
		this.key0 = key0;
	}

	@Override
	public int getKey1() {
		return key1;
	}

	@Override
	public void setKey1(final int key1) {
		this.key1 = key1;
	}

}
