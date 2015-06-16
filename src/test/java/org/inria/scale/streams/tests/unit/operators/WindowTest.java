package org.inria.scale.streams.tests.unit.operators;

import static org.hamcrest.Matchers.contains;
import static org.inria.scale.streams.tests.utils.Matchers.listThat;
import static org.inria.scale.streams.tests.utils.TupleUtils.tupleWith;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.inria.scale.streams.base.MultiActiveServiceFactory;
import org.inria.scale.streams.operators.Window;
import org.inria.scale.streams.windows.StaticWindowStrategyFactory;
import org.inria.scale.streams.windows.WindowStrategy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.proactive.Body;
import org.objectweb.proactive.multiactivity.MultiActiveService;

public class WindowTest {

	private static final String SOME_JSON_CONFIGURATION = "";
	private static final long WAIT_RUNNING_ACTIVITY = 200;

	private final Body body = mock(Body.class);

	private final MultiActiveServiceFactory serviceFactory = mock(MultiActiveServiceFactory.class);
	private final MultiActiveService service = mock(MultiActiveService.class);

	private final StaticWindowStrategyFactory strategyFactory = mock(StaticWindowStrategyFactory.class);
	private final WindowStrategy strategy = mock(WindowStrategy.class);

	private final Window window = new Window(serviceFactory, strategyFactory);

	@Before
	public void setBodyMockUp() {
		when(body.isActive()).thenReturn(true);
	}

	@Before
	public void setFactoryMocksUp() {
		when(serviceFactory.createService(body)).thenReturn(service);
		when(strategyFactory.createFrom(anyString())).thenReturn(strategy);
	}

	@Before
	public void setWindowUp() {
		window.setWindowConfiguration(SOME_JSON_CONFIGURATION);
	}

	@After
	public void setBodyMockDown() {
		when(body.isActive()).thenReturn(false);
	}

	@Test
	public void shouldInitializeTheStrategyAfterRunningActivity() throws Exception {
		window.setWindowConfiguration(SOME_JSON_CONFIGURATION);
		verify(strategy, never()).initialize(window);

		// run in a different thread as it is just a synchronous call in the test
		final Thread thread = new Thread() {
			@Override
			public void run() {
				window.runActivity(body);
			}
		};
		thread.start();

		Thread.sleep(WAIT_RUNNING_ACTIVITY);

		verify(strategy).initialize(window);
	}

	@Test
	public void shouldTearDownAndInitializeTheStrategyAfterSettingANewConfigurationIfItWasAlreadyRunning()
			throws Exception {
		final WindowStrategy newStrategy = mock(WindowStrategy.class);
		when(strategyFactory.createFrom(anyString())).thenReturn(newStrategy);

		// run in a different thread as it is just a synchronous call in the test
		final Thread thread = new Thread() {
			@Override
			public void run() {
				window.runActivity(body);
			}
		};
		thread.start();

		Thread.sleep(WAIT_RUNNING_ACTIVITY);

		window.setWindowConfiguration(SOME_JSON_CONFIGURATION);
		verify(strategy).tearDown();
		verify(newStrategy).initialize(window);
	}

	@Test
	public void shouldCheckWithTheStrategyWhenANewSetOfTuplesIsReceived() throws Exception {
		window.receive(anyInt(), Arrays.asList(tupleWith(1), tupleWith('a'), tupleWith("hello")));
		verify(strategy).check(listThat(contains(tupleWith(1), tupleWith('a'), tupleWith("hello"))));
	}

}
