package org.inria.scale.streams.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.inria.scale.streams.base.WindowConfigurationObject.COUNT;
import static org.inria.scale.streams.base.WindowConfigurationObject.MINIMUM_TIME_BETWEEN_EXECUTIONS;
import static org.inria.scale.streams.base.WindowConfigurationObject.TIME;
import static org.inria.scale.streams.base.WindowConfigurationObject.TUMBLING;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.inria.scale.streams.base.WindowConfigurationObject;
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
	public void returnsSameTuplesListWhenIsTimeTumblingWindow() throws Exception {
		final WindowConfigurationObject configuration = new WindowConfigurationBuilder() //
				.setType(TUMBLING) //
				.setTumblingType(TIME) //
				.build();

		final List<Unit<Integer>> tuples = Arrays.asList(Unit.with(1), Unit.with(2), Unit.with(3));

		assertThat(configuration.selectTuples(tuples).getTuplesToProcess(), is(equalTo(tuples)));
	}

}
