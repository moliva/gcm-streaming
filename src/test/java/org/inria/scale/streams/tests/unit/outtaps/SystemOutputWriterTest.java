package org.inria.scale.streams.tests.unit.outtaps;

import static org.inria.scale.streams.tests.utils.TupleUtils.tupleWith;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import org.inria.scale.streams.outtaps.SystemOutputWriter;
import org.javatuples.Tuple;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

public class SystemOutputWriterTest {

	private final PrintStream output = mock(PrintStream.class);

	private final SystemOutputWriter operator = new SystemOutputWriter();

	@Before
	public void setSystemOut() {
		System.setOut(output);
	}

	@Test
	public void shouldWriteTuplesJoinedByBlankPerLineInOrder() throws Exception {
		final List<Tuple> tuples = Arrays.asList( //
				tupleWith("a", 1), //
				tupleWith("b", 2), //
				tupleWith("c", 3));

		operator.processTuples(tuples);

		final InOrder inOrder = inOrder(output);
		inOrder.verify(output).println("a 1");
		inOrder.verify(output).println("b 2");
		inOrder.verify(output).println("c 3");
	}

}
