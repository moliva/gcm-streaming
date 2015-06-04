package org.inria.scale.streams.tests.unit;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.inria.scale.streams.base.WindowConfigurationObject.COUNT;
import static org.inria.scale.streams.base.WindowConfigurationObject.MINIMUM_TIME_BETWEEN_EXECUTIONS;
import static org.inria.scale.streams.base.WindowConfigurationObject.TIME;
import static org.inria.scale.streams.base.WindowConfigurationObject.TUMBLING;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.inria.scale.streams.base.WindowConfigurationObject;
import org.inria.scale.streams.base.WindowConfigurationObject.TupleSelection;
import org.inria.scale.streams.tests.builders.WindowConfigurationBuilder;
import org.javatuples.Unit;
import org.junit.Test;

public class WindowConfigurationObjectTest {

	@Test
	public void computesCorrectlyTimeBetweenExecutionsForTimeTumblingWindow() throws Exception {
		final WindowConfigurationObject configuration = new WindowConfigurationBuilder() //
				.setType(TUMBLING) //
				.setTumblingType(TIME) //
				.setMilliseconds(500) //
				.build();

		assertThat(configuration.getTimeBetweenExecutions(), is(equalTo(500l)));
	}

	@Test
	public void computesMinimumDefaultTimeBetweenExecutionsForCountTumblingWindow() throws Exception {
		final WindowConfigurationObject configuration = new WindowConfigurationBuilder() //
				.setType(TUMBLING) //
				.setTumblingType(COUNT) //
				.build();

		assertThat(configuration.getTimeBetweenExecutions(), is(equalTo(MINIMUM_TIME_BETWEEN_EXECUTIONS)));
	}

	@Test
	public void returnsSameTuplesCollectionAndEmptyNewQueueWhenIsTimeTumblingWindow() throws Exception {
		final WindowConfigurationObject configuration = new WindowConfigurationBuilder() //
				.setType(TUMBLING) //
				.setTumblingType(TIME) //
				.build();

		final List<Unit<Integer>> tuples = Arrays.asList(Unit.with(1), Unit.with(2), Unit.with(3));

		final TupleSelection<Unit<Integer>> selection = configuration.selectTuples(tuples);

		assertThat(selection.getTuplesToProcess(), is(equalTo(tuples)));
		assertThat(selection.getNewTuples(), is(empty()));
	}

	@Test
	public void returnsEmptyTuplesCollectionAndSameNewQueueWhenIsCountTumblingWindowAndNotComplete() throws Exception {
		final WindowConfigurationObject configuration = new WindowConfigurationBuilder() //
				.setType(TUMBLING) //
				.setTumblingType(COUNT) //
				.setCount(5) //
				.build();

		final List<Unit<Integer>> tuples = Arrays.asList(Unit.with(1), Unit.with(2), Unit.with(3));

		final TupleSelection<Unit<Integer>> selection = configuration.selectTuples(tuples);

		assertThat(selection.getTuplesToProcess(), is(empty()));
		assertThat(selection.getNewTuples(), is(equalTo(tuples)));
	}

	@Test
	public void returnsSameTuplesCollectionAndEmptyNewQueueWhenIsCountTumblingWindowAndIsComplete() throws Exception {
		final WindowConfigurationObject configuration = new WindowConfigurationBuilder() //
				.setType(TUMBLING) //
				.setTumblingType(COUNT) //
				.setCount(3) //
				.build();

		final List<Unit<Integer>> tuples = Arrays.asList(Unit.with(1), Unit.with(2), Unit.with(3));

		final TupleSelection<Unit<Integer>> selection = configuration.selectTuples(tuples);

		assertThat(selection.getTuplesToProcess(), is(equalTo(tuples)));
		assertThat(selection.getNewTuples(), is(empty()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void returnsTuplesCollectionContainingTheFirstElementsAndNewQueueWithLastElmentsWhenIsCountTumblingWindowAndIsLargerThanComplete()
			throws Exception {
		final WindowConfigurationObject configuration = new WindowConfigurationBuilder() //
				.setType(TUMBLING) //
				.setTumblingType(COUNT) //
				.setCount(2) //
				.build();

		final List<Unit<Integer>> tuples = Arrays.asList(Unit.with(1), Unit.with(2), Unit.with(3));

		final TupleSelection<Unit<Integer>> selection = configuration.selectTuples(tuples);

		assertThat(selection.getTuplesToProcess(), contains(Unit.with(1), Unit.with(2)));
		assertThat(selection.getNewTuples(), contains(Unit.with(3)));
	}
}
