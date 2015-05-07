package org.inria.scale.streams.operators;

import java.util.Comparator;
import java.util.List;

import org.inria.scale.streams.base.BaseOperator;
import org.inria.scale.streams.configuration.SortByConfiguration;
import org.javatuples.Tuple;

import com.google.common.collect.FluentIterable;

public class SortBy extends BaseOperator implements SortByConfiguration {

	private static final String DESCENDING_ORDER = "desc";

	private int position;
	private String order;

	@Override
	protected List<Tuple> processTuples(final List<Tuple> tuplesToProcess) {
		return FluentIterable.from(tuplesToProcess).toSortedList(new Comparator<Tuple>() {

			@SuppressWarnings("unchecked")
			@Override
			public int compare(final Tuple tuple1, final Tuple tuple2) {
				return order(((Comparable<Object>) tuple1.getValue(position)).compareTo(tuple2.getValue(position)));
			}
		});
	}

	private int order(final int comparisonResult) {
		return DESCENDING_ORDER.equalsIgnoreCase(order) ? -comparisonResult : comparisonResult;
	}

	// //////////////////////////////////////////////
	// ******* SortByConfiguration *******
	// //////////////////////////////////////////////

	@Override
	public void setTupleComponent(final int position) {
		this.position = position;
	}

	@Override
	public int getTupleComponent() {
		return position;
	}

	@Override
	public void setOrder(final String order) {
		this.order = order;
	}

	@Override
	public String getOrder() {
		return order;
	}
}
