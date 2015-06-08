package org.inria.scale.streams.tests.unit;


@SuppressWarnings("unchecked")
public class TumblingwindowStrategyTest {
//
//	@Test
//	public void computesCorrectlyTimeBetweenExecutionsForTimeTumblingWindow() throws Exception {
//		final WindowConfigurationObject configuration = new WindowConfigurationBuilder() //
//				.setType(TUMBLING) //
//				.setTumblingType(TIME) //
//				.setMilliseconds(500) //
//				.build();
//
//		assertThat(configuration.getTimeBetweenExecutions(), is(equalTo(500l)));
//	}
//
//	@Test
//	public void computesMinimumDefaultTimeBetweenExecutionsForCountTumblingWindow() throws Exception {
//		final WindowConfigurationObject configuration = new WindowConfigurationBuilder() //
//				.setType(TUMBLING) //
//				.setTumblingType(COUNT) //
//				.build();
//
//		assertThat(configuration.getTimeBetweenExecutions(), is(equalTo(MINIMUM_TIME_BETWEEN_EXECUTIONS)));
//	}
//
//	@Test
//	public void returnsSameTuplesCollectionAndEmptyNewQueueWhenIsTimeTumblingWindow() throws Exception {
//		final WindowConfigurationObject configuration = new WindowConfigurationBuilder() //
//				.setType(TUMBLING) //
//				.setTumblingType(TIME) //
//				.build();
//
//		final Queue<Unit<Integer>> tuples = createQueue(Unit.with(1), Unit.with(2), Unit.with(3));
//
//		final TupleSelection<Unit<Integer>> selection = configuration.selectTuples(tuples);
//
//		assertThat(selection.getTuplesToProcess(), is(equalTo(tuples)));
//		assertThat(selection.getNewTuples(), is(empty()));
//	}
//
//	@Test
//	public void returnsEmptyTuplesCollectionAndSameNewQueueWhenIsCountTumblingWindowAndNotComplete() throws Exception {
//		final WindowConfigurationObject configuration = new WindowConfigurationBuilder() //
//				.setType(TUMBLING) //
//				.setTumblingType(COUNT) //
//				.setCount(5) //
//				.build();
//
//		final Queue<Unit<Integer>> tuples = createQueue(Unit.with(1), Unit.with(2), Unit.with(3));
//
//		final TupleSelection<Unit<Integer>> selection = configuration.selectTuples(tuples);
//
//		assertThat(selection.getTuplesToProcess(), is(empty()));
//		assertThat(selection.getNewTuples(), is(equalTo(tuples)));
//	}
//
//	@Test
//	public void returnsSameTuplesCollectionAndEmptyNewQueueWhenIsCountTumblingWindowAndIsComplete() throws Exception {
//		final WindowConfigurationObject configuration = new WindowConfigurationBuilder() //
//				.setType(TUMBLING) //
//				.setTumblingType(COUNT) //
//				.setCount(3) //
//				.build();
//
//		final Queue<Unit<Integer>> tuples = createQueue(Unit.with(1), Unit.with(2), Unit.with(3));
//
//		final TupleSelection<Unit<Integer>> selection = configuration.selectTuples(tuples);
//
//		assertThat(selection.getTuplesToProcess(), is(equalTo(tuples)));
//		assertThat(selection.getNewTuples(), is(empty()));
//	}
//
//	@Test
//	public void returnsTuplesCollectionContainingTheFirstElementsAndNewQueueWithLastElmentsWhenIsCountTumblingWindowAndIsLargerThanComplete()
//			throws Exception {
//		final WindowConfigurationObject configuration = new WindowConfigurationBuilder() //
//				.setType(TUMBLING) //
//				.setTumblingType(COUNT) //
//				.setCount(2) //
//				.build();
//
//		final Queue<Unit<Integer>> tuples = createQueue(Unit.with(1), Unit.with(2), Unit.with(3));
//
//		final TupleSelection<Unit<Integer>> selection = configuration.selectTuples(tuples);
//
//		assertThat(selection.getTuplesToProcess(), contains(Unit.with(1), Unit.with(2)));
//		assertThat(selection.getNewTuples(), contains(Unit.with(3)));
//	}
//
//	@Test
//	public void shouldBeTimeGovernedWhenIsTimeTumblingWindow() throws Exception {
//		final WindowConfigurationObject configuration = new WindowConfigurationBuilder() //
//				.setType(TUMBLING) //
//				.setTumblingType(TIME) //
//				.build();
//
//		assertThat(configuration.isTimeGoverned(), is(true));
//	}
//
//	@Test
//	public void shouldNotBeTimeGovernedWhenIsCountTumblingWindow() throws Exception {
//		final WindowConfigurationObject configuration = new WindowConfigurationBuilder() //
//				.setType(TUMBLING) //
//				.setTumblingType(COUNT) //
//				.build();
//
//		assertThat(configuration.isTimeGoverned(), is(false));
//	}
//
//	// //////////////////////////////////////////////
//	// ******* Utils *******
//	// //////////////////////////////////////////////
//
//	private <T extends Tuple> Queue<T> createQueue(final T... tuples) {
//		return new ConcurrentLinkedQueue<>(Arrays.asList(tuples));
//	}

}
