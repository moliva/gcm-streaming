package org.inria.scale.streams.operators;

import java.util.Comparator;
import java.util.List;

import org.inria.scale.streams.base.BaseOperator;
import org.inria.scale.streams.configuration.SortByConfiguration;
import org.javatuples.Tuple;

import com.google.common.collect.FluentIterable;

/**
 * 
 * <p>
 * Sorts the tuples in a single batch by the values in an index defined by
 * <code>tupleComponent</code> in each tuple. The order can be descending, from
 * biggest to lowest, using the <code>desc</code> order, or ascending,
 * otherwise.
 * </p>
 * <p>
 * The tuple component must be {@link Comparable}.
 * </p>
 * 
 * @author moliva
 *
 */
public class SortBy extends BaseOperator implements SortByConfiguration {

	public static final String DESCENDING_ORDER = "desc";
	public static final String ASCENDING_ORDER = "asc";

	private int position;
	private String order;

	@Override
	public List<Tuple> processTuples(final List<Tuple> tuplesToProcess) {
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
